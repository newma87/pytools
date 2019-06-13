package {{ package }}.enum;

/**
 * Author: {{ author }}
 * Create: {{ time }}
 */

{% if comment %}
// {{ comment }}
{% endif %}
public {{type}} {{name}} {
    {% for prop in properties %}

    {{prop.name}}{%+ if prop.value != None %}({{prop.value}}){% endif %}, {%+ if prop.comment %}// {{prop.comment}}{% endif %}
    
    {% endfor %}
}
