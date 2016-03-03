package com.dbsys.rs.patient.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.DateUtil;
import com.dbsys.rs.Kelas;
import com.dbsys.rs.Penanggung;
import com.dbsys.rs.patient.entity.Pasien;
import com.dbsys.rs.patient.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.patient.entity.Pasien.Pendaftaran;
import com.dbsys.rs.patient.entity.Pasien.StatusPasien;
import com.dbsys.rs.patient.entity.Pasien.Perawatan;
import com.dbsys.rs.patient.entity.Penduduk;
import com.dbsys.rs.patient.entity.Unit;
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
	public void ubahPenanggung(Long id, Penanggung penanggung) {
		pasienRepository.ubahPenanggung(id, penanggung);
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
		return pasienRepository.findByRuangPerawatan_Id(id);
	}

	@Override
	public List<Pasien> cari(String keyword) {
		return pasienRepository.findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContaining(keyword, keyword, keyword, keyword);
	}

	@Override
	public List<Pasien> cari(String keyword, StatusPasien status) {
		keyword = String.format("%s%s%s", "%", keyword, "%");
		return pasienRepository.findByPenduduk_NamaContainingOrPenduduk_KodeContainingOrPenduduk_NikContainingOrKodeContainingAndStatus(keyword, keyword, keyword, keyword, status);
	}

	@Override
	@Transactional(readOnly = false)
	public void hapus(Long id) {
		pasienRepository.delete(id);
	}

	@Override
	public Pasien simpan(Pasien pasien) {
		return pasienRepository.save(pasien);
	}

	@Override
	public List<Pasien> getByMedrek(String nomorMedrek) {
		return pasienRepository.findByPenduduk_Kode(nomorMedrek);
	}
	
	@Override
	public List<Pasien> getTunggakan(String nomorMedrek) {
		List<Pasien> list = pasienRepository.findByPenduduk_Kode(nomorMedrek);
		List<Pasien> listTunggakan = new ArrayList<>();

		// menyatakan sebagai tunggakan
		boolean flag;
		for (Pasien pasien : list) {
			flag = true;
			
			if (Penanggung.BPJS.equals(pasien.getPenanggung())) {
				flag = false; // bukan tunggakan jika tanggungan BPJS
			} else {
				
				long hutang = pasien.getTotalTagihan() - pasien.getCicilan();
				if (hutang <= 0)
					flag = false; // bukan tunggakan jika hutang <= 0
			}

			if (flag)
				listTunggakan.add(pasien);
		}
		
		return listTunggakan;
	}

	@Override
	public List<Pasien> get(Date awal, Date akhir) {
		return pasienRepository.findByTanggalMasukBetween(awal, akhir);
	}

	@Override
	public List<Pasien> get(Date awal, Date akhir, Pendaftaran pendaftaran) {
		return pasienRepository.findByTanggalMasukBetweenAndPendaftaran(awal, akhir, pendaftaran);
	}

	@Override
	public void updatePerawatanPasien(String nomorPasien, Long idUnit) {
		Pasien pasien;
		try {
			pasien = pasienRepository.findByKode(nomorPasien); // Cek jika nomor pasien merupakan kode salah satu pasien.
		} catch (PersistenceException ex) {
			throw new PersistenceException("Nomor pasien yang anda masukkan tidak terdaftar");
		}
		
		Unit unit = null;
		if (idUnit != null && !idUnit.equals(new Long(0)))
			unit = unitRepository.findOne(idUnit);

		Perawatan perawatan = Perawatan.RAWAT_INAP;
		// Jika  pasien bukan rawat inap && tanggal rawat inap null,
		// maka pasien baru masuk sal (baru rawat inap).
		// Atur tanggal rawat inap, tipe perawatan.
		if (!perawatan.equals(pasien.getTipePerawatan()) && pasien.getTanggalRawatInap() == null) {
			Date tanggalRawatInap = DateUtil.getDate();

			pasienRepository.updatePasien(nomorPasien, unit, tanggalRawatInap, perawatan);
		} else {
			pasienRepository.updatePasien(nomorPasien, unit);
		}
	}
}
