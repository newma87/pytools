package {{Package}}.common.auditor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-05 18:56:10
 * Description:
 * @author newma
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @CreatedDate
    @Column(name = "gmt_created", nullable = false, updatable = false)
    protected Instant gmtCreated;

    @LastModifiedDate
    @Column(name = "gmt_modified", nullable = true)
    protected  Instant gmtModified;

    public void setGmtCreated(Instant gmtCreated){
        this.gmtCreated = gmtCreated;
    }

    public void setGmtModified(Instant gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Instant getGmtCreated() {
        return gmtCreated;
    }

    public Instant getGmtModified() {
        return gmtModified;
    }
}
