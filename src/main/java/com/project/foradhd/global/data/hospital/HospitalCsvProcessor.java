package com.project.foradhd.global.data.hospital;

import com.project.foradhd.domain.hospital.persistence.entity.Doctor;
import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.domain.hospital.persistence.repository.DoctorRepository;
import com.project.foradhd.domain.hospital.persistence.repository.HospitalRepository;
import com.project.foradhd.global.util.CSVUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.foradhd.global.util.StringUtil.addPrefix;
import static com.project.foradhd.global.util.StringUtil.strip;

@RequiredArgsConstructor
@Component
public class HospitalCsvProcessor implements CommandLineRunner {

    private static final String HOSPITAL_CSV_PATH = "/static/csv/hospital.csv";
    private static final String DOCTOR_CSV_PATH = "/static/csv/doctor.csv";

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<String[]> hospitalCsvList = CSVUtil.readAll(HOSPITAL_CSV_PATH);
        List<String[]> doctorCsvList = CSVUtil.readAll(DOCTOR_CSV_PATH);

        Map<Integer, Hospital> hospitalByCsvId = hospitalCsvList.stream()
                .collect(Collectors.toMap(hospital -> Integer.valueOf(hospital[0]), hospital -> {
                    GeometryFactory geometryFactory = new GeometryFactory();
                    double latitude = Double.parseDouble(hospital[2]);
                    double longitude = Double.parseDouble(hospital[3]);
                    Coordinate coordinate = new Coordinate(longitude, latitude);
                    String name = strip(hospital[1]);
                    String address = strip(hospital[4]);
                    String phone = addPrefix(hospital[5], "0");

                    return Hospital.builder()
                            .name(name)
                            .location(geometryFactory.createPoint(coordinate))
                            .address(address)
                            .phone(phone)
                            .build();
                }));

        List<Hospital> hospitalList = hospitalByCsvId.values().stream().toList();
        List<Doctor> doctorList = doctorCsvList.stream()
                .map(doctor -> {
                    Integer hospitalCsvId = Integer.valueOf(strip(doctor[0].split("\\.")[0]));
                    Hospital hospital = hospitalByCsvId.get(hospitalCsvId);
                    String name = strip(doctor[2]);
                    String profile = strip(doctor[3]);
                    String image = strip(doctor[4]);

                    return Doctor.builder()
                            .hospital(hospital)
                            .name(name)
                            .image(image)
                            .profile(profile)
                            .build();
                })
                .toList();

        hospitalRepository.saveAll(hospitalList);
        doctorRepository.saveAll(doctorList);
    }
}
