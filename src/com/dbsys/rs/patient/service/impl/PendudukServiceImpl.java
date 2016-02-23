package com.dbsys.rs.patient.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.DateUtil;
import com.dbsys.rs.patient.entity.Penduduk;
import com.dbsys.rs.patient.repository.PendudukRepository;
import com.dbsys.rs.patient.service.PendudukService;

@Service
@Transactional(readOnly = true)
public class PendudukServiceImpl implements PendudukService {

	@Autowired
	private PendudukRepository pendudukRepository;
	
	@Override
	@Transactional(readOnly = false)
	public Penduduk save(Penduduk penduduk) {
		if (penduduk.getKode() == null)
			penduduk.generateKode();
		if (penduduk.getTanggalDaftar() == null)
			penduduk.setTanggalDaftar(DateUtil.getDate());
		
		return pendudukRepository.save(penduduk);
	}

	@Override
	public Penduduk get(Long id) {
		return pendudukRepository.findOne(id);
	}

	@Override
	public List<Penduduk> get(String keyword) {
		return pendudukRepository.findByKodeContainingOrNikContainingOrNamaContaining(keyword, keyword, keyword);
	}

	@Override
	public List<Penduduk> getAll() {
		return pendudukRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		pendudukRepository.delete(id);
	}
}
