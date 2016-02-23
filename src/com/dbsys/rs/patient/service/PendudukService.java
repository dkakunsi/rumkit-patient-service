package com.dbsys.rs.patient.service;

import java.util.List;

import com.dbsys.rs.patient.entity.Penduduk;

/**
 * Interface untuk mengelola data penduduk.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public interface PendudukService {

	/**
	 * Menyimpan penduduk.
	 * 
	 * @param pendudukEntity
	 * 
	 * @return penduduk yang sudah tersimpan
	 */
	Penduduk save(Penduduk penduduk);

	/**
	 * Mengambil penduduk berdasarkan id.
	 * 
	 * @param id
	 * 
	 * @return penduduk
	 */
	Penduduk get(Long id);

	/**
	 * Mengambil daftar penduduk yang sesuai dengan keyword.
	 * 
	 * @param keyword
	 * 
	 * @return daftar penduduk
	 */
	List<Penduduk> get(String keyword);

	/**
	 * Mengambil semua penduduk.
	 * 
	 * @return daftar penduduk
	 */
	List<Penduduk> getAll();

	/**
	 * Menghapus penduduk berdasarkan id.
	 * 
	 * @param id
	 */
	void delete(Long id);

}
