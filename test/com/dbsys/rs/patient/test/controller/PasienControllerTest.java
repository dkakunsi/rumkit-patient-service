package com.dbsys.rs.patient.test.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.patient.repository.PasienRepository;
import com.dbsys.rs.patient.service.PasienService;
import com.dbsys.rs.patient.service.PendudukService;
import com.dbsys.rs.patient.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PasienControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private long count;

	@Autowired
	private PasienService pasienService;
	@Autowired
	private PendudukService pendudukService;
	@Autowired
	private PasienRepository pasienRepository;

	private Pasien pasien;
	private Penduduk penduduk;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		count = pasienRepository.count();
		
		penduduk = new Penduduk();
		penduduk.setAgama("Kristen");
		penduduk.setDarah("O");
		penduduk.setKelamin(Kelamin.PRIA);
		penduduk.setNama("Penduduk 1");
		penduduk.setNik("Nik 1");
		penduduk.setTanggalLahir(DateUtil.getDate());
		penduduk.setTelepon("Telepon");
		penduduk = pendudukService.save(penduduk);

		pasien = pasienService.daftar(penduduk.getId(), Tanggungan.BPJS);

		assertEquals(count + 1, pasienRepository.count());
	}	

	@Test
	public void testDaftar() throws Exception {
		this.mockMvc.perform(
				post(String.format("/pasien/penduduk/%d/tanggungan/%s", penduduk.getId(), Tanggungan.BPJS))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
		
		assertEquals(count + 2, pasienRepository.count());
	}

	@Test
	public void testConvertPasien() throws Exception {
		this.mockMvc.perform(
				put(String.format("/pasien/%d/kelas/%s", pasien.getId(), Kelas.I))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
		
		assertEquals(count + 1, pasienRepository.count());
	}

	@Test
	public void testGetLong() throws Exception {
		this.mockMvc.perform(
				get(String.format("/pasien/%d", pasien.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetString() throws Exception {
		this.mockMvc.perform(
				get(String.format("/pasien/kode/%s", pasien.getKode()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}
}
