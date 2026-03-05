package pdl.backend;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class ImageControllerTests {

	@MockitoBean
	private ImageDao imageDAO;
	@MockitoBean
	private Image image;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getImageShouldReturnSuccess() throws Exception {
		when(imageDAO.retrieve(0)).thenReturn(Optional.ofNullable(image));
		this.mockMvc.perform(get("/images/0")).andExpect(status().isOk());
		verify(imageDAO).retrieve(0);
	}

	@Test
	public void getImageShouldReturnNotFound() throws Exception {
		// TODO
	}

	@Test
	public void addImageShouldReturnSuccess() throws Exception {
		// TODO
	}

	@Test
	public void addImageShouldReturnUnsupportedMediaType() throws Exception {
		// TODO
	}

	@Test
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
		// TODO
	}

	@Test
	public void deleteImageShouldReturnNotFound() throws Exception {
		// TODO
	}

	@Test
	public void deleteImageShouldReturnSuccess() throws Exception {
		// TODO
	}
	
	@Test
	public void getImageListShouldReturnSuccess() throws Exception {
		// TODO
	}
	
}
