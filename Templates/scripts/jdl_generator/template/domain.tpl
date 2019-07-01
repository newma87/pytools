package {{ package }}.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import {{ package }}.common.auditor.Auditable;

/**
 * @author: {{ author }}
 * @create: {{ time }}
 */

{% if comment %}
/** 
 * {{ comment }}
 */
{% endif %}
@Entity
{% if alias %}
@Table(name = "{{alias}}")
{% endif %}
public {{type}} {{name}} extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;
    {% for prop in properties %}
        {% if prop.comment %}
    /**
     * {{prop.comment}}
     */
        {% endif %}
        {% if prop.relationship %}
            {% if prop.relationship == "ManyToMany" %}
                {% if prop.isLeader %}
    @{{prop.relationship}}
    @JoinTable(name = "{{prop.relationshipAlias}}", 
                joinColumns = @JoinColumn(name = "{{prop.slavePropAlias}}"),
                inverseJoinColumns = @JoinColumn(name = "{{prop.extend.alias}}"))
                {% else %}
    @{{prop.relationship}}(mappedBy = "{{prop.slavePropName}}")
                {% endif %}
            {% elif prop.relationship == "OneToOne" %}
                {% if prop.isLeader %}
    @{{prop.relationship}}
    @JoinColumn(name = "{{prop.extend.alias}}") 
                {% endif %}
            {% elif prop.relationship == "ManyToOne" and prop.isLeader %}
    @{{prop.relationship}}
    @JoinColumn(name = "{{prop.extend.alias}}") 
            {% endif %}
        {% else %}
            {% if prop.extend.isPrimary %}
    @Id
                {% if prop.extend.isAutoIncrease %}
    @GeneratedValue(strategy = GenerationType.IDENTITY)
                {% endif %}
            {% endif %}
    @Column(name = "{{prop.extend.alias}}"{%+ if prop.isFixedLength() %}, length = {{prop.getFixedLength()}}{% endif %}{%+ if not prop.extend.isNullable %}, nullable = false{% endif %})       
            {% if not prop.isFixedLength() and (prop.extend.min or prop.extend.max) %}
    @Size({%+ if prop.extend.min %}min = {{prop.extend.min}}{% endif %}{%+ if prop.extend.max %}, max = {{prop.extend.max}}{% endif %})
            {% endif %}
        {% endif %}{%+ if prop.type == "Blob" %}
    @Lob
    {% endif -%}
    {% if not (prop.relationship == "OneToMany" or (prop.relationship == "ManyToOne" and not prop.isLeader) or (prop.relationship == "OneToOne" and not prop.isLeader)) %}
    private {{prop.type|jpatype}} {{prop.name}};

    {% endif %}
    {% endfor %}

    // getter and setter
    {%for prop in properties%}

    {% if not (prop.relationship == "OneToMany" or (prop.relationship == "ManyToOne" and not prop.isLeader) or (prop.relationship == "OneToOne" and not prop.isLeader)) %}
    public {{prop.type|jpatype}} get{{prop.name|captain}}() {
        return this.{{prop.name}};
    }

    public void set{{prop.name|captain}}({{prop.type|jpatype}} {{prop.name}}) {
        this.{{prop.name}} = {{prop.name}};
    }
    {% endif %}
    {%endfor%}

    @Override
    public int hashCode() {
        return Objects.hashCode(get{{primary.name|captain}}());
    }

    @Override
    public String toString() {
        return "{ {{name}}: { " +
            "'{{primary.name}}': '" + get{{primary.name|captain}}() + "'" +
        {% for prop in properties %}
            {% if not prop.extend.isPrimary %}
                {% if not prop.relationship %}
            ", '{{prop.name}}': '" + get{{prop.name|captain}}() + "'" +
                {% endif %}
            {% endif %}
        {% endfor %}
            " }}";
    }
} 
