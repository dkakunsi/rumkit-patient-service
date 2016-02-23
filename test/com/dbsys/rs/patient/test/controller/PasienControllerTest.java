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
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Unit;
import com.dbsys.rs.lib.entity.Pasien.Pendaftaran;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.patient.repository.PasienRepository;
import com.dbsys.rs.patient.repository.UnitRepository;
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
	@Autowired
	private UnitRepository unitRepository;

	private Pasien pasien;
	private Penduduk penduduk;
	private Unit tujuan;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		count = pasienRepository.count();
		
		tujuan = new Unit();
		tujuan.setNama("Unit");
		tujuan.setBobot(1f);
		tujuan.setTipe(Unit.TipeUnit.APOTEK_FARMASI);
		tujuan = unitRepository.save(tujuan);
		
		penduduk = new Penduduk();
		penduduk.setAgama("Kristen");
		penduduk.setDarah("O");
		penduduk.setKelamin(Kelamin.PRIA);
		penduduk.setNama("Penduduk 1111111111");
		penduduk.setNik("Nik 1111111111");
		penduduk.setTanggalLahir(DateUtil.getDate());
		penduduk.setTelepon("Telepon");
		penduduk = pendudukService.save(penduduk);

		pasien = pasienService.daftar(penduduk.getId(), Penanggung.BPJS, DateUtil.getDate(), "PAS01", Pendaftaran.LOKET, Kelas.I, tujuan.getId());
		pasienService.updateRuangPerawatan(pasien.getKode(), tujuan.getId());

		assertEquals(count + 1, pasienRepository.count());
	}	

	@Test
	public void testDaftarLoket() throws Exception {
		this.mockMvc.perform(
				post(String.format("/pasien/penduduk/%d/penanggung/%s/tanggal/%s/kode/%s/pendaftaran/%s/kelas/%s/tujuan/%d", penduduk.getId(), Penanggung.BPJS, DateUtil.getDate(), null, Pendaftaran.LOKET, Kelas.I, tujuan.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.model.status").value("PERAWATAN"))
			.andExpect(jsonPath("$.model.kelas").value("I"))
			.andExpect(jsonPath("$.model.penanggung").value("BPJS"))
			.andExpect(jsonPath("$.model.pendaftaran").value("LOKET"))
			.andExpect(jsonPath("$.model.tipePerawatan").value("RAWAT_JALAN"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
		
		assertEquals(count + 2, pasienRepository.count());
	}

	@Test
	public void testDaftarUgd() throws Exception {
		this.mockMvc.perform(
				post(String.format("/pasien/penduduk/%d/penanggung/%s/tanggal/%s/kode/%s/pendaftaran/%s/kelas/%s/tujuan/%d", penduduk.getId(), Penanggung.BPJS, DateUtil.getDate(), null, Pendaftaran.UGD, Kelas.I, tujuan.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.model.status").value("PERAWATAN"))
			.andExpect(jsonPath("$.model.kelas").value("I"))
			.andExpect(jsonPath("$.model.penanggung").value("BPJS"))
			.andExpect(jsonPath("$.model.pendaftaran").value("UGD"))
			.andExpect(jsonPath("$.model.tipePerawatan").value("UGD"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
		
		assertEquals(count + 2, pasienRepository.count());
	}

	@Test
	public void testKeluar() throws Exception {
		this.mockMvc.perform(
				put(String.format("/pasien/%d/tanggal/%s/jam/%s/keadaan/%s/status/%s", pasien.getId(), DateUtil.getDate(), DateUtil.getTime(), KeadaanPasien.SEMBUH, StatusPasien.KELUAR))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"))
			.andExpect(jsonPath("$.model.tipePerawatan").value("RAWAT_JALAN"))
			.andExpect(jsonPath("$.model.keadaan").value("SEMBUH"))
			.andExpect(jsonPath("$.model.status").value("KELUAR"));
		
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

	@Test
	public void testGetByPenduduk() throws Exception {
		this.mockMvc.perform(
				get(String.format("/pasien/penduduk/%d", penduduk.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetByUnit() throws Exception {
		this.mockMvc.perform(
				get(String.format("/pasien/unit/%d", tujuan.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testUpdateRuangPerawatan() throws Exception {
		this.mockMvc.perform(
				put(String.format("/pasien/%s/unit/%d", pasien.getKode(), tujuan.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("SUCCESS"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
		
		this.mockMvc.perform(
				get(String.format("/pasien/%d", pasien.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"))
			.andExpect(jsonPath("$.model.ruangPerawatan.nama").value(tujuan.getNama()));
	}

	@Test
	public void testUpdateRuangPerawatan_KodeNotFound() throws Exception {
		this.mockMvc.perform(
				put(String.format("/pasien/%s/unit/%d", "kode asal", tujuan.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ERROR"))
			.andExpect(jsonPath("$.message").value("Nomor pasien yang anda masukkan tidak terdaftar"));
	}

	@Test
	public void testUpdateRuangPerawatan_UnitNotFound() throws Exception {
		this.mockMvc.perform(
				put(String.format("/pasien/%s/unit/%d", pasien.getKode(), 1111))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ERROR"))
			.andExpect(jsonPath("$.message").value("Data tidak ditemukan"));
	}
}
