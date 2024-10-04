package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

import com.fasterxml.jackson.databind.JsonNode;

public class RunCliente {
	public static void main(String[] args) throws NotBoundException, IOException {
		
		Cliente cliente = new Cliente();
		String localizacion_cliente = "main";
		cliente.startCliente(localizacion_cliente);
		System.out.println("Bienvenido a la plataforma de adopción");
		
		while(true) {	
			System.out.println("1.- Ver posibles adoptantes"); // recuperacion de errores ok
			System.out.println("2.- Buscar cachorrines ");
			System.out.println("3.- Adoptar cachorrines");
			System.out.println("4.- Modificar adoptante");
			System.out.println("5.- Borrar adoptante");// implementar recuperacion de errores
			System.out.println("6.- Salir");
			System.out.println("Ingrese la opción: ");
			char bufferInput = (char) System.in.read();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			if (bufferInput == '1') {
				try {
					 cliente.getPersonas();
				} catch(Exception e) {
					localizacion_cliente = "backup";
					cliente.startCliente(localizacion_cliente);
				}
				System.out.println("###");
			} else if (bufferInput == '2') {
				
					System.out.println("Seleccione filtro de busqueda cachorrin");
					System.out.println("1.- Comuna");
					System.out.println("2.- Edad maxima");
					System.out.println("3.- Tipo de animal");
					System.out.println("4.- Salir");
					System.in.skip(System.in.available());
		            
					String entrada = br.readLine();
			        int opcion = Integer.parseInt(entrada);
					switch(opcion) {

		              // caso para buscar por comuna
		              case 1:
		            	try {
		            		  cliente.buscarComuna();
		 				} catch(Exception e) {
		 					localizacion_cliente = "backup";
		 					cliente.startCliente(localizacion_cliente);
		 				}
		                break;

		              // caso para buscar por edad
		              case 2:
		            	  try {
		            		  cliente.buscarEdad();
		            	  } catch(Exception e) {
		            		  localizacion_cliente = "backup";
		            		  cliente.startCliente(localizacion_cliente);
		            	  }
		                break;

		              // caso para buscar por tipo de animal
		              case 3:
		            	  try {
		            		  cliente.buscarAnimal();
		            	  } catch(Exception e) {
		            		  localizacion_cliente = "backup";
		            		  cliente.startCliente(localizacion_cliente);
		            	  }
		               break;
		               
		          
		              case 4:
		            	  break;
		               
		              default:
		                System.out.println("Opcion incorrecta.");
					}
				
				
			}
			else if(bufferInput == '3')
			{
				try {
          		  cliente.GuardarUsuarioAdopcion();
				} catch(Exception e) {
					localizacion_cliente = "backup";
					cliente.startCliente(localizacion_cliente);
				}
			}
			else if(bufferInput == '4')
			{
				try {
	          		  cliente.modificarAdoptador();
					} catch(Exception e) {
						localizacion_cliente = "backup";
						cliente.startCliente(localizacion_cliente);
					}
				
			}
			else if(bufferInput == '5')
			{
				try {
	          		  cliente.borrarUsuarioAdop();;
					} catch(Exception e) {
						localizacion_cliente = "backup";
						cliente.startCliente(localizacion_cliente);
					}
			}
			else if(bufferInput == '6')
			{
				System.exit(0);
			}
			
			// Limpiar el buffer
            System.in.skip(System.in.available());
            
		}
	}

}
