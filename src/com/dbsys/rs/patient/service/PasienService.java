package com.dbsys.rs.patient.service;

import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Pasien;

/**
 * Interface untuk mengelola data pasien, baik Rawat Jalan maupun Rawat Inap.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public interface PasienService {

	/**
	 * Daftarkan pasien. Secara otomatis, pasien adalah pasien rawat jalan.
	 * Rawat inap hanya dapat diubah oleh poliklinik.
	 * 
	 * @param pasien
	 * 
	 * @return pasien yang sudah tersimpan
	 */
	Pasien daftar(Long idPenduduk, Tanggungan tanggungan);

	/**
	 * Ubah pasien Rawat Jalan menjadi Pasien Rawat Inap.
	 * 
	 * @param pasien
	 * @param kelas
	 * 
	 * @return pasien yang sudah di-convert
	 */
	Pasien convert(Long idPasien, Kelas kelas);

	/**
	 * Mengambil data pasien.
	 * 
	 * @param id
	 * 
	 * @return pasien
	 */
	Pasien get(Long id);

	/**
	 * Mengambil data pasien berdasarkan kode.
	 * 
	 * @param kode
	 * 
	 * @return pasien
	 */
	Pasien get(String kode);
	
}
