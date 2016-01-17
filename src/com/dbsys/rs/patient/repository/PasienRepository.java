package com.dbsys.rs.patient.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;

public interface PasienRepository extends JpaRepository<Pasien, Long> {

	Pasien findByKode(String kode);

	List<Pasien> findByPenduduk_Id(Long id);

	List<Pasien> findByPerawatan_Unit_Id(Long id);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Pasien p SET p.tanggalKeluar = :tanggal, p.keadaan = :keadaan, p.status = :status WHERE p.id = :id")
	void keluar(@Param("id") Long id, @Param("tanggal") Date tanggal, @Param("keadaan") KeadaanPasien keadaan, @Param("status") StatusPasien status);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Pasien p SET p.kelas = :kelas WHERE p.id = :id")
	void ubahKelas(@Param("id") Long id, @Param("kelas") Kelas kelas);

	List<Pasien> findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContaining(
			String nama, String nomorRekamMedik, String nik, String nomorPasien);

	@Query("FROM Pasien p WHERE (p.penduduk.nama LIKE :nama OR p.penduduk.kode LIKE :medrek OR p.penduduk.nik LIKE :nik OR p.kode LIKE :kode) AND p.status = :status")
	List<Pasien> findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContainingAndStatus(
			@Param("nama") String nama, @Param("medrek") String nomorRekamMedik, 
			@Param("nik") String nik, @Param("kode") String nomorPasien, @Param("status") StatusPasien status);

}
