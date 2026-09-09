package com.aura.config;

import com.aura.model.Clinic;
import com.aura.model.ClinicMember;
import com.aura.model.User;
import com.aura.repo.ClinicMemberRepository;
import com.aura.repo.ClinicRepository;
import com.aura.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(ClinicRepository clinics, ClinicMemberRepository members, UserRepository users) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User doctor = users.findByEmail("doctor@aura.vn").orElseGet(() -> {
                User u = new User();
                u.setEmail("doctor@aura.vn");
                u.setPasswordHash(encoder.encode("doctor123"));
                u.setFullName("Dr. AURA Demo");
                u.setRole("DOCTOR");
                u.setEnabled(true);
                return users.save(u);
            });

            users.findByEmail("clinic@aura.vn").orElseGet(() -> {
                User u = new User();
                u.setEmail("clinic@aura.vn");
                u.setPasswordHash(encoder.encode("clinic123"));
                u.setFullName("AURA Vision Clinic");
                u.setRole("CLINIC");
                u.setEnabled(true);
                return users.save(u);
            });

            Clinic clinic;
            if (clinics.count() == 0) {
                clinic = new Clinic();
                clinic.setName("AURA Vision Clinic");
                clinic.setAddress("12 Nguyen Hue, Ho Chi Minh City");
                clinic.setLicenseNumber("VN-CLINIC-26001");
                clinic.setStatus("APPROVED");
                clinic = clinics.save(clinic);
            } else {
                clinic = clinics.findAll().get(0);
            }

            if (members.count() == 0) {
                saveMember(members, clinic.getId(), doctor.getFullName(), doctor.getEmail(), "DOCTOR");
                saveMember(members, clinic.getId(), "Nguyen Van Nam", "nam@example.com", "PATIENT");
                saveMember(members, clinic.getId(), "Le Thi Mai", "mai@example.com", "PATIENT");
                saveMember(members, clinic.getId(), "Tran Minh Duc", "duc@example.com", "PATIENT");
            }
        };
    }

    private void saveMember(ClinicMemberRepository repo, Long clinicId, String name, String email, String role) {
        ClinicMember member = new ClinicMember();
        member.setClinicId(clinicId);
        member.setFullName(name);
        member.setEmail(email);
        member.setRole(role);
        member.setStatus("ACTIVE");
        repo.save(member);
    }
}
