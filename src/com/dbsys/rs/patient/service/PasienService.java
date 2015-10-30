package com.dbsys.rs.patient.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;

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
	 * @param idPenduduk
	 * @param tanggungan
	 * @param tanggal jika null generate.
	 * @param kode jika {@code tanggungan} = BPJS, kode = kode BPJS. Selain itu, generate.
	 * 
	 * @return pasien yang sudah tersimpan
	 */
	Pasien daftar(Long idPenduduk, Tanggungan tanggungan, Date tanggal, String kode);

	/**
	 * Pasien keluar rumah sakit.
	 * 
	 * @param id
	 * @param tanggal
	 * @param jam
	 * @return
	 */
	Pasien keluar(Long id, Date tanggal, Time jam, KeadaanPasien keadaan, StatusPasien status);

	/**
	 * Membayar tagihan pasien. 
	 * Dapat menyicil.
	 * 
	 * @param id
	 * @param jumlah
	 * 
	 * @return pasien
	 */
	Pasien bayar(Long id, Long jumlah);

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

	/**
	 * Mengambil data pasien berdasarkan id penduduk.
	 * 
	 * @param id
	 * 
	 * @return daftar pasien
	 */
	List<Pasien> getByPenduduk(Long id);

	/**
	 * Mengambil data pasien berdasarkan id unit.
	 * 
	 * @param id
	 * @return
	 */
	List<Pasien> getByUnit(Long id);

	/**
	 * Cari pasien berdasarkan kata kunci. 
	 * Kata kunci dapat berupa nama, nomor rekam medik, nik, kode pasien.
	 * 
	 * @param keyword
	 * 
	 * @return daftar pasien
	 */
	List<Pasien> cari(String keyword);
	
}
