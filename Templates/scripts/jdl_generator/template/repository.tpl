package {{ package }}.repository;

import {{package}}.domain.{{domain}};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: {{ author }}
 * Create: {{ time }}
 */

@SuppressWarnings("unused")
@Repository
public interface {{name}} extends JpaRepository<{{domain}}, {{primaryType}}> {

}
