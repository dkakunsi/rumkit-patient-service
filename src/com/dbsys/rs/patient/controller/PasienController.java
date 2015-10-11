package com.dbsys.rs.patient.controller;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbsys.rs.lib.ApplicationException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.patient.service.PasienService;

@Controller
@RequestMapping("/pasien")
public class PasienController {

	@Autowired
	private PasienService pasienService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public EntityRestMessage<Pasien> daftar(@RequestBody Pasien pasien) throws ApplicationContextException, PersistenceException {
		pasien = pasienService.daftar(pasien);
		return EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/kelas/{kelas}")
	@ResponseBody
	public EntityRestMessage<Pasien> convertPasien(@RequestBody Pasien pasien, @PathVariable Kelas kelas) throws ApplicationContextException, PersistenceException {
		pasien = pasienService.convert(pasien, kelas);
		return EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public EntityRestMessage<Pasien> get(@PathVariable Long id) throws ApplicationException, PersistenceException {
		Pasien pasien = pasienService.get(id);
		return  EntityRestMessage.createPasien(pasien);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/test/test")
	@ResponseBody
	public RestMessage test() throws ApplicationException, PersistenceException {
		return RestMessage.success();
	}
}
