package net.wssouza.springboot;


import net.wssouza.springboot.service.FilesStorageService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.verify;


@SpringBootTest
//@ContextConfiguration(classes = SpringbootRestfulWebservicesApplication.class)
class SpringbootRestfulWebservicesApplicationTests {

	@Autowired
	private CommandLineRunner initRunner;

	@Autowired
	private FilesStorageService storageService;

//	@Disabled
//	@Test
//	void testInitBean() {
//		// Verificar se o CommandLineRunner foi injetado
//		assertThat(initRunner).isNotNull();
//
//		// Imprimir informações sobre o CommandLineRunner
//		System.out.println("CommandLineRunner: " + initRunner);
//
//		// Executar o método sob teste
//		try {
//			initRunner.run();
//		} catch (Exception e) {
//			System.out.println("initRunner.run: " + e.getMessage());
//			throw new RuntimeException(e);
//		}
//
////		// Verificar se o método deleteAll foi chamado
////		verify(storageService).deleteAll();
////
////		// Verificar se o método init foi chamado
////		verify(storageService).init();
//	}

}
