package com.project.foradhd.domain.user.persistence.enums;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", 1), USER("ROLE_USER", 2),
    GUEST("ROLE_GUEST", 3), ANONYMOUS("ROLE_ANONYMOUS", 4);

    private static final String HIERARCHY_DELIMITER = " > ";
    private final String name;
    private final Integer priority;

    public static String hierarchy() {
        return Arrays.stream(Role.values())
            .sorted(comparing(Role::getPriority))
            .map(Role::getName)
            .collect(Collectors.joining(HIERARCHY_DELIMITER));
    }
}
