package com.dbsys.rs.patient.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.Pendaftaran;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;
import com.dbsys.rs.lib.entity.Pasien.Perawatan;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Unit;
import com.dbsys.rs.patient.repository.PasienRepository;
import com.dbsys.rs.patient.repository.PendudukRepository;
import com.dbsys.rs.patient.repository.UnitRepository;
import com.dbsys.rs.patient.service.PasienService;

@Service
@Transactional(readOnly = true)
public class PasienServiceImpl implements PasienService {

	@Autowired
	private PasienRepository pasienRepository;
	@Autowired
	private PendudukRepository pendudukRepository;
	@Autowired
	private UnitRepository unitRepository;
	
	@Override
	@Transactional(readOnly = false)
	public Pasien daftar(Long idPenduduk, Penanggung penanggung, Date tanggal, String kode, Pendaftaran pendaftaran, Kelas kelas, Long idTujuan) {
		Pasien pasien = new Pasien();
		pasien.setStatus(StatusPasien.PERAWATAN);
		pasien.setPendaftaran(pendaftaran);
		pasien.setKelas(kelas);
		pasien.setPenanggung(penanggung);

		Penduduk penduduk = pendudukRepository.findOne(idPenduduk);
		pasien.setPenduduk(penduduk);

		Unit tujuan = unitRepository.findOne(idTujuan);
		pasien.setTujuan(tujuan);
		
		if (tanggal == null)
			tanggal = DateUtil.getDate();
		pasien.setTanggalMasuk(tanggal);
		
		Perawatan perawatan = (Pendaftaran.UGD.equals(pendaftaran)) ? Perawatan.UGD : Perawatan.RAWAT_JALAN;
		pasien.setTipePerawatan(perawatan);

		pasien.setKode(kode);
		if (kode == null || kode.equals("") || kode.equals("null"))
			pasien.generateKode();
		
		return pasienRepository.save(pasien);
	}

	@Override
	@Transactional(readOnly = false)
	public Pasien keluar(Long id, Date tanggal, Time jam, KeadaanPasien keadaan, StatusPasien status) {
		pasienRepository.keluar(id, tanggal, keadaan, status);
		return pasienRepository.findOne(id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Pasien bayar(Long id, Long jumlah) {
		Pasien pasien = pasienRepository.findOne(id);
		pasien.bayar(jumlah);

		return pasienRepository.save(pasien);
	}
	
	@Override
	public void ubahKelas(Long id, Kelas kelas) {
		pasienRepository.ubahKelas(id, kelas);
	}

	@Override
	public Pasien get(Long id) {
		return pasienRepository.findOne(id);
	}

	@Override
	public Pasien get(String kode) {
		return pasienRepository.findByKode(kode);
	}

	@Override
	public List<Pasien> getByPenduduk(Long id) {
		return pasienRepository.findByPenduduk_Id(id);
	}

	@Override
	public List<Pasien> getByUnit(Long id) {
		return pasienRepository.findByPerawatan_Unit_Id(id);
	}

	@Override
	public List<Pasien> cari(String keyword) {
		return pasienRepository.findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContaining(keyword, keyword, keyword, keyword);
	}

	@Override
	public List<Pasien> cari(String keyword, StatusPasien status) {
		return pasienRepository.findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContainingAndStatus(keyword, keyword, keyword, keyword, status);
	}

	@Override
	public void hapus(Long id) {
		pasienRepository.delete(id);
	}
}
