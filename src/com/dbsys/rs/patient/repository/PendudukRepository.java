package com.dbsys.rs.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbsys.rs.lib.entity.Penduduk;

public interface PendudukRepository extends JpaRepository<Penduduk, Long> {

	/**
	 * Mencari penduduk berdasarkan keyword.
	 * 
	 * @param kode
	 * @param nik
	 * @param nama
	 * 
	 * @return daftar penduduk
	 */
	List<Penduduk> findByKodeContainingOrNikContainingOrNamaContaining(String kode, String nik, String nama);

}
