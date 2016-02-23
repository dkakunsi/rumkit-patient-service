package com.dbsys.rs.patient.test.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

import com.dbsys.rs.DateUtil;
import com.dbsys.rs.patient.entity.Penduduk;
import com.dbsys.rs.patient.entity.Penduduk.Kelamin;
import com.dbsys.rs.patient.repository.PendudukRepository;
import com.dbsys.rs.patient.service.PendudukService;
import com.dbsys.rs.patient.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PendudukControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private long count;
	
	@Autowired
	private PendudukService pendudukService;
	@Autowired
	private PendudukRepository pendudukRepository;
	
	private Penduduk penduduk;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		count = pendudukRepository.count();
		
		penduduk = new Penduduk();
		penduduk.setAgama("Kristen");
		penduduk.setDarah("O");
		penduduk.setKelamin(Kelamin.PRIA);
		penduduk.setNama("Penduduk 1");
		penduduk.setNik("Nik 1");
		penduduk.setTanggalLahir(DateUtil.getDate());
		penduduk.setTelepon("Telepon");
		pendudukService.save(penduduk);

		assertEquals(count + 1, pendudukRepository.count());
	}	

	@Test
	public void testSave() throws Exception {
		this.mockMvc.perform(
				post("/penduduk")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"agama\": \"Kristen\","
						+ "\"kode\": \"000111\","
						+ "\"darah\": \"O\","
						+ "\"kelamin\": \"PRIA\","
						+ "\"nama\":\"Penduduk 2\","
						+ "\"nik\":\"nik 2\","
						+ "\"tanggalLahir\":\"1991-12-05\","
						+ "\"telepon\":\"telepon 2\"}")
						
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"))
			.andDo(print());
		
		
		assertEquals(count + 2, pendudukRepository.count());
	}

	@Test
	public void testDelete() throws Exception {
		this.mockMvc.perform(
				delete(String.format("/penduduk/%d", penduduk.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("SUCCESS"))
			.andExpect(jsonPath("$.message").value("Berhasil"));

		assertEquals(count, pendudukRepository.count());
	}

	@Test
	public void testGetLong() throws Exception {
		this.mockMvc.perform(
				get(String.format("/penduduk/%d", penduduk.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetStringKode() throws Exception {
		this.mockMvc.perform(
				get(String.format("/penduduk/keyword/%s", penduduk.getKode()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetStringNik() throws Exception {
		this.mockMvc.perform(
				get(String.format("/penduduk/keyword/%s", penduduk.getNik()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetStringNama() throws Exception {
		this.mockMvc.perform(
				get(String.format("/penduduk/keyword/%s", penduduk.getNama()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetAll() throws Exception {
		this.mockMvc.perform(
				get("/penduduk")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}
}
