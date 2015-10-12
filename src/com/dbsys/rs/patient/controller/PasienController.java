package com.dbsys.rs.patient.controller;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbsys.rs.lib.ApplicationException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.patient.service.PasienService;

@Controller
@RequestMapping("/pasien")
public class PasienController {

	@Autowired
	private PasienService pasienService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/penduduk/{idPenduduk}/tanggungan/{tanggunganString}")
	@ResponseBody
	public EntityRestMessage<Pasien> daftar(@PathVariable Long idPenduduk, @PathVariable String tanggunganString) throws ApplicationContextException, PersistenceException {
		Pasien pasien = pasienService.daftar(idPenduduk, Tanggungan.valueOf(tanggunganString));
		return EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/kelas/{kelas}")
	@ResponseBody
	public EntityRestMessage<Pasien> convertPasien(@PathVariable Long id, @PathVariable String kelasString) throws ApplicationContextException, PersistenceException {
		Pasien pasien = pasienService.convert(id, Kelas.valueOf(kelasString));
		return EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public EntityRestMessage<Pasien> get(@PathVariable Long id) throws ApplicationException, PersistenceException {
		Pasien pasien = pasienService.get(id);
		return  EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/kode/{kode}")
	@ResponseBody
	public EntityRestMessage<Pasien> get(String kode) throws ApplicationException, PersistenceException {
		Pasien pasien = pasienService.get(kode);
		return  EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/test/test")
	@ResponseBody
	public RestMessage test() throws ApplicationException, PersistenceException {
		return RestMessage.success();
	}
}
