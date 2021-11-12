package com.adi.e_posyandu.activity.model;

public class Jadwal {
    private int id, kelurahan_id;
    private String tgl_kegiatan, waktu_kegiatan, keterangan, kelurahan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKelurahan_id() {
        return kelurahan_id;
    }

    public void setKelurahan_id(int kelurahan_id) {
        this.kelurahan_id = kelurahan_id;
    }

    public String getTgl_kegiatan() {
        return tgl_kegiatan;
    }

    public void setTgl_kegiatan(String tgl_kegiatan) {
        this.tgl_kegiatan = tgl_kegiatan;
    }

    public String getWaktu_kegiatan() {
        return waktu_kegiatan;
    }

    public void setWaktu_kegiatan(String waktu_kegiatan) {
        this.waktu_kegiatan = waktu_kegiatan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }
}
