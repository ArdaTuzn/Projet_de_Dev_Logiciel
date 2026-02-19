package pdl.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao implements Dao<Image>, InitializingBean {
  
  private final JdbcTemplate jdbc;
  private final Map<Long, Image> images = new HashMap<>();

  public ImageDao(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public Optional<Image> retrieve(final long id) {
    Image image = images.get(id);
    return Optional.ofNullable(image);
  }

  @Override
  public List<Image> retrieveAll() {
    ArrayList<Image> arrayList = new ArrayList<Image>(images.values());
    return arrayList;
  }

  @Override
  public void create(final Image img) {
    images.put(img.getId(), img);
    String type = MediaTypeFactory.getMediaType(img.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
    String sql = "INSERT INTO images (name, type) VALUES (?,?)";
    jdbc.update(sql, img.getName(), type);
    try {
      FileOutputStream fos = new FileOutputStream("/Users/ardatuzun/Documents/Projet_de_Developpement_Logiciel/Projet_de_Dev_Logiciel/racine/backend/src/main/resources/" + img.getName());
      fos.write(img.getData());
      fos.close();
    } catch (IOException e) {
      System.out.println("An error occurred: " + e.getMessage());
    }
  }

  @Override
  public void update(final Image img, final String[] params) {
    if (images.containsKey(img.getId())) {
      img.setName(params[0]);
      images.put(img.getId(), img);
    }
  }

  @Override
  public void delete(final Image img) {
    images.remove(img.getId());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    String sql = "CREATE TABLE IF NOT EXISTS images (id BIGSERIAL PRIMARY KEY, name character varying(255), type character varying(255))";
    jdbc.update(sql);
    sql = "SELECT * FROM images";
    RowMapper<Image> imageRowMapper = (r,i) -> {
      Long imageId = r.getLong("id");
      String filename = r.getString("name");
      //data
      String path = "/Users/ardatuzun/Documents/Projet_de_Developpement_Logiciel/Projet_de_Dev_Logiciel/racine/backend/src/main/resources/";
      byte[] data = null;
      try {
        java.nio.file.Path filepath = java.nio.file.Paths.get(path + filename);
        if (java.nio.file.Files.exists(filepath)) {
          data = java.nio.file.Files.readAllBytes(filepath);
        }
      } catch (IOException e) {
        System.out.println(filename + "reading error: " + e.getMessage());
      }
      Image newImage = new Image(imageId, filename, data);
      images.put(imageId, newImage);
      return newImage;
    };
    if (!images.isEmpty()) {
        long maxIdInDb = images.keySet().stream().max(Long::compare).get();
        Image.setCount(maxIdInDb + 1); 
    }
    jdbc.query(sql, imageRowMapper);
    
  }
}
