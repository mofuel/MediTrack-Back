package com.MediTrack.persistance.entity;

import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    @Test
    void testSetAndGetId() {
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        assertEquals(1L, specialty.getId());
    }

    @Test
    void testSetAndGetNombre() {
        Specialty specialty = new Specialty();
        specialty.setNombre("Cardiología");
        assertEquals("Cardiología", specialty.getNombre());
    }

    @Test
    void testSetAndGetMedicos() {
        Specialty specialty = new Specialty();

        MedicProfile medic1 = new MedicProfile();
        medic1.setId(1L);

        MedicProfile medic2 = new MedicProfile();
        medic2.setId(2L);

        Set<MedicProfile> medicos = Set.of(medic1, medic2);
        specialty.setMedicos(medicos);

        assertEquals(2, specialty.getMedicos().size());
        assertTrue(specialty.getMedicos().contains(medic1));
        assertTrue(specialty.getMedicos().contains(medic2));
    }

    @Test
    void testEmptySpecialty() {
        Specialty specialty = new Specialty();
        assertNull(specialty.getId());
        assertNull(specialty.getNombre());
        assertNull(specialty.getMedicos());
    }
}
