package pdl.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class ImageDao implements Dao<Image>, InitializingBean {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  private final Map<Long, Image> images = new HashMap<>();
  private Image defaultImage;

  public ImageDao() {
    // placez une image test.jpg dans le dossier "src/main/resources" du projet
    final ClassPathResource imgFile = new ClassPathResource("immortal2.jpg");
    byte[] fileContent;
    try {
      fileContent = Files.readAllBytes(imgFile.getFile().toPath());
      Image img = new Image("cat.jpg", fileContent);
      this.defaultImage = img; 
      images.put(img.getId(), img); 
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<Image> retrieve(final long id) {
    return Optional.ofNullable(images.get(id));
  }

  @Override
  public List<Image> retrieveAll() {
    return new ArrayList<Image>(images.values());
  }  

  @Override
  public void create(final Image img) {
    images.put(img.getId(),img);
    jdbcTemplate.update("INSERT INTO images (id,name) VALUES (?,?)", img.getId(), img.getName());
    String folderPath = "src/main/resources/images/";
    String fileName = img.getName();

    try (FileOutputStream fos = new FileOutputStream(folderPath + fileName)) {
        fos.write(img.getData()); 
    } catch (IOException e) {
        System.err.println("Write Error: " + e.getMessage());
    }
  }

  @Override
  public void update(final Image img, final String[] params) {
    // Not used
  }

  @Override
  public void delete(final Image img) {
    images.remove(img.getId());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // Drop table
    jdbcTemplate.execute("DROP TABLE IF EXISTS images");
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS images (id bigserial PRIMARY KEY, name character varying(255))");
    jdbcTemplate.update("INSERT INTO images (id,name) VALUES (?,?)", defaultImage.getId(), defaultImage.getName());
  }
}
