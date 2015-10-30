package com.dbsys.rs.patient.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;
import com.dbsys.rs.lib.entity.Pasien.Type;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.patient.repository.PasienRepository;
import com.dbsys.rs.patient.repository.PendudukRepository;
import com.dbsys.rs.patient.service.PasienService;

@Service
@Transactional
public class PasienServiceImpl implements PasienService {

	@Autowired
	private PasienRepository pasienRepository;
	@Autowired
	private PendudukRepository pendudukRepository;
	
	@Override
	public Pasien daftar(Long idPenduduk, Tanggungan tanggungan, Date tanggal, String kode) {
		Penduduk penduduk = pendudukRepository.findOne(idPenduduk);
		
		if (tanggal == null)
			tanggal = DateUtil.getDate();

		Pasien pasien = new Pasien();
		pasien.setPenduduk(penduduk);
		pasien.setTanggungan(tanggungan);
		pasien.setStatus(StatusPasien.OPEN);
		pasien.setTipe(Type.RAWAT_JALAN);
		pasien.setTanggalMasuk(tanggal);
		pasien.setKode(kode);

		if (tanggungan.equals(Tanggungan.UMUM) || kode == null || kode.equals("") || kode.equals("null"))
			pasien.generateKode();
		
		return pasienRepository.save(pasien);
	}

	@Override
	public Pasien keluar(Long id, Date tanggal, Time jam, KeadaanPasien keadaan, StatusPasien status) {
		pasienRepository.keluar(id, tanggal, keadaan, status);
		return pasienRepository.findOne(id);
	}
	
	@Override
	public Pasien bayar(Long id, Long jumlah) {
		Pasien pasien = pasienRepository.findOne(id);
		pasien.bayar(jumlah);

		return pasienRepository.save(pasien);
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
}
