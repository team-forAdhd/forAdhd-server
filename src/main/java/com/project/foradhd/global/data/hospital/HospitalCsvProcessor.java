package com.project.foradhd.global.data.hospital;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.domain.hospital.persistence.repository.DoctorRepository;
import com.project.foradhd.domain.hospital.persistence.repository.HospitalRepository;
import com.project.foradhd.global.client.GooglePlacesClient;
import com.project.foradhd.global.client.dto.response.GooglePlaceListResponse;
import com.project.foradhd.global.util.CSVUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.foradhd.global.util.StringUtil.strip;

@Profile("init")
@RequiredArgsConstructor
@Component
public class HospitalCsvProcessor implements CommandLineRunner {

    private static final String HOSPITAL_CSV_INPUT_PATH = "/static/csv/hospital.csv";
    private static final String DOCTOR_CSV_INPUT_PATH = "/static/csv/doctor.csv";
    private static final String HOSPITAL_CSV_OUTPUT_PATH = "src/main/resources/static/csv/hospital_result.csv";
    private static final String DOCTOR_CSV_OUTPUT_PATH = "src/main/resources/static/csv/doctor_result.csv";

    private static final int HOSPITAL_ID_IDX = 0;
    private static final int HOSPITAL_NAME_IDX = 1;
    private static final int HOSPITAL_PHONE_IDX = 2;
    private static final int HOSPITAL_ADDRESS_IDX = 3;
    private static final int HOSPITAL_LATITUDE_IDX = 4;
    private static final int HOSPITAL_LONGITUDE_IDX = 5;
    private static final int HOSPITAL_DELETED_IDX = 6;
    private static final int HOSPITAL_PLACE_ID_IDX = 7;

    private static final int DOCTOR_ID_IDX = 1;
    private static final int DOCTOR_NAME_IDX = 2;
    private static final int DOCTOR_PROFILE_IDX = 3;
    private static final int DOCTOR_IMAGE_IDX = 4;
    private static final int DOCTOR_DELETED_IDX = 5;

    private static final String UUID_REGEX = "^[0-9a-fA-F]{32}$";

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final GooglePlacesClient googlePlacesClient;

    @Override
    public void run(String... args) throws Exception {
        List<String[]> hospitalCsvInputList = CSVUtil.readAll(HOSPITAL_CSV_INPUT_PATH);
        List<String[]> doctorCsvInputList = CSVUtil.readAll(DOCTOR_CSV_INPUT_PATH);
        String[] hospitalCsvHeader = CSVUtil.readHeader(HOSPITAL_CSV_INPUT_PATH);
        String[] doctorCsvHeader = CSVUtil.readHeader(DOCTOR_CSV_INPUT_PATH);

        Map<String, Hospital> hospitalMapByCsvId = hospitalCsvInputList.stream()
                .collect(Collectors.toMap(hospital -> strip(hospital[HOSPITAL_ID_IDX]), hospital -> {
                    GeometryFactory geometryFactory = new GeometryFactory();
                    double latitude = Double.parseDouble(hospital[HOSPITAL_LATITUDE_IDX]);
                    double longitude = Double.parseDouble(hospital[HOSPITAL_LONGITUDE_IDX]);
                    Coordinate coordinate = new Coordinate(longitude, latitude);

                    String hospitalId = strip(hospital[HOSPITAL_ID_IDX]);
                    String name = strip(hospital[HOSPITAL_NAME_IDX]);
                    String address = strip(hospital[HOSPITAL_ADDRESS_IDX]);
                    String phone = strip(hospital[HOSPITAL_PHONE_IDX]).replace("-", "");
                    boolean deleted = Boolean.parseBoolean(strip(hospital[HOSPITAL_DELETED_IDX]));
                    String placeId = StringUtils.hasText(strip(hospital[HOSPITAL_PLACE_ID_IDX])) ?
                            strip(hospital[HOSPITAL_PLACE_ID_IDX]) : requestPlaceId(name);

                    return Hospital.builder()
                            .id(hospitalId.matches(UUID_REGEX) ? hospitalId : null) //UUID 포맷인지 확인(DB에 저장된 경우인지)
                            .name(name)
                            .location(geometryFactory.createPoint(coordinate))
                            .address(address)
                            .phone(phone)
                            .placeId(placeId)
                            .deleted(deleted)
                            .build();
                    }, (e1, e2) -> e1, LinkedHashMap::new));

        List<Hospital> hospitalList = hospitalMapByCsvId.values().stream().toList();
        List<Doctor> doctorList = doctorCsvInputList.stream()
                .map(doctor -> {
                    String hospitalId = strip(doctor[HOSPITAL_ID_IDX]);
                    Hospital hospital = hospitalMapByCsvId.get(hospitalId);
                    String doctorId = strip(doctor[DOCTOR_ID_IDX]);
                    String name = strip(doctor[DOCTOR_NAME_IDX]);
                    String profile = strip(doctor[DOCTOR_PROFILE_IDX]);
                    String image = strip(doctor[DOCTOR_IMAGE_IDX]);
                    boolean deleted = Boolean.parseBoolean(strip(doctor[DOCTOR_DELETED_IDX]));

                    return Doctor.builder()
                            .hospital(hospital)
                            .id(doctorId.matches(UUID_REGEX) ? doctorId : null) //UUID 포맷인지 확인(DB에 저장된 경우인지)
                            .name(name)
                            .image(image)
                            .profile(profile)
                            .deleted(deleted)
                            .build();
                })
                .toList();

        hospitalRepository.saveAll(hospitalList);
        doctorRepository.saveAll(doctorList);

        List<String[]> hospitalCsvOutputList = hospitalList.stream()
                .map(hospital -> new String[]{hospital.getId(), hospital.getName(), hospital.getPhone(), hospital.getAddress(),
                        String.valueOf(hospital.getLocation().getY()), String.valueOf(hospital.getLocation().getX()),
                        String.valueOf(hospital.getDeleted() ? 1 : 0), hospital.getPlaceId()})
                .toList();
        List<String[]> doctorCsvOutputList = doctorList.stream()
                .map(doctor -> new String[]{doctor.getHospital().getId(), doctor.getId(), doctor.getName(),
                        doctor.getProfile(), doctor.getImage(), String.valueOf(doctor.getDeleted() ? 1 : 0)})
                .toList();

        CSVUtil.writeAll(HOSPITAL_CSV_OUTPUT_PATH, hospitalCsvHeader, hospitalCsvOutputList);
        CSVUtil.writeAll(DOCTOR_CSV_OUTPUT_PATH, doctorCsvHeader, doctorCsvOutputList);
    }

    private String requestPlaceId(String name) {
        GooglePlaceListResponse googlePlaceListResponse = googlePlacesClient.searchPlaces(name);
        if (googlePlaceListResponse.getPlaces().size() == 1) {
            return googlePlaceListResponse.getPlaces().get(0).getId();
        }
        return null;
    }
}
