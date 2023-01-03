package com.example.application.data.service;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Hospital;

import com.example.application.data.entity.Patient;
import com.example.application.data.entity.Type;
import com.example.application.data.repository.HospitalRepository;
import com.example.application.data.repository.DoctorRepository;

import com.example.application.data.repository.PatientRepository;
import com.example.application.data.repository.TypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class HospitalService {

    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;
    private final TypeRepository typeRepository;
    private final PatientRepository patientRepository;

    public HospitalService(DoctorRepository doctorRepository,
                      HospitalRepository hospitalRepository,
                      TypeRepository typeRepository,
                      PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.hospitalRepository = hospitalRepository;
        this.typeRepository = typeRepository;
        this.patientRepository = patientRepository;
    }

    public List<Doctor> findAllDoctors(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return doctorRepository.findAll();
        } else {
            return doctorRepository.search(stringFilter);
        }
    }

    public long countDoctors() {
        return doctorRepository.count();
    }

    public void deleteDoctor(Doctor doctor) {
        doctorRepository.delete(doctor);
    }

    public void saveDoctor(Doctor doctor) {
        if (doctor == null) {
            System.err.println("Doctor is null. Are you sure you have connected your form to the application?");
            return;
        }
        doctorRepository.save(doctor);
    }

    public List<Hospital> findAllHospitals() {
        return hospitalRepository.findAll();
    }
    public List<Doctor> findAllDoctors(){
        return doctorRepository.findAll();
    }

    public List<Type> findAllTypes(){
        return typeRepository.findAll();
    }

    public List<Patient> findAllPatients(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return patientRepository.findAll();
        } else {
            return patientRepository.search(stringFilter);
        }
    }

    public void savePatient(Patient patient) {
        if (patient == null) {
            System.err.println("Doctor is null. Are you sure you have connected your form to the application?");
            return;
        }
        patientRepository.save(patient);
    }
    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }

}

