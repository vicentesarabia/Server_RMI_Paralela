package common;

import java.io.Serializable;

public class Persona implements Serializable {

	  private static final long serialVersionUID = 1L;
	  private int idUsuario;
	  private String direccion;
	  private String nombre;
	  private String apellido;
	  private String comuna;
	  private int idMascota;

	  // 

	  public Persona() {}
	  public Persona(int idUsuario, String comuna, String direccion, String nombre, String apellido, int idMascota) {
	      this.setIdUsuario(idUsuario);
	      this.setComuna(comuna);
	      this.setDireccion(direccion);
	      this.setNombre(nombre);
	      this.setApellido(apellido);
	      this.setIdMascota(idMascota);
	  }
	  public Persona( String comuna, String direccion, String nombre, String apellido, int idMascota) {
	      this.setComuna(comuna);
	      this.setDireccion(direccion);
	      this.setNombre(nombre);
	      this.setApellido(apellido);
	      this.setIdMascota(idMascota);
	  }
	  
	  public int getIdUsuario() {
	      return idUsuario;}
	  public void setIdUsuario(int idUsuario) {
	      this.idUsuario = idUsuario;}
	  public String getNombre() {
	      return nombre;}
	  public void setNombre(String nombre) {
	      this.nombre = nombre;}
	  public String getApellido() {
	      return apellido;}
	  public void setApellido(String apellido) {
	      this.apellido = apellido;}
	  public String getComuna() {
	      return comuna;}
	  public void setComuna(String comuna) {
	      this.comuna = comuna;}
	  public String getDireccion() {
	      return direccion;}
	  public void setDireccion(String direccion) {
	      this.direccion = direccion;}
	  public int getIdMascota() {
	      return idMascota;}
	  public void setIdMascota(int idMascota) {
	      this.idMascota = idMascota;}

}

