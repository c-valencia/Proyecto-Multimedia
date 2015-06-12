/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author cristian
 */
@Entity
@Table(name = "opcion")
@NamedQueries({
    @NamedQuery(name = "Opcion.findAll", query = "SELECT o FROM Opcion o"),
    @NamedQuery(name = "Opcion.findByIdOpcion", query = "SELECT o FROM Opcion o WHERE o.idOpcion = :idOpcion"),
    @NamedQuery(name = "Opcion.findByDescripcion", query = "SELECT o FROM Opcion o WHERE o.descripcion = :descripcion")})
public class Opcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_opcion")
    private Integer idOpcion;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOpcionCorrecta")
    private List<Pregunta> preguntaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOpcionSeleccionada")
    private List<RespuestaXExamen> respuestaXExamenList;
    @JoinColumn(name = "id_pregunta_asociada", referencedColumnName = "id_pregunta")
    @ManyToOne(optional = false)
    private Pregunta idPreguntaAsociada;

    public Opcion() {
    }

    public Opcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public Opcion(Integer idOpcion, String descripcion) {
        this.idOpcion = idOpcion;
        this.descripcion = descripcion;
    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Pregunta> getPreguntaList() {
        return preguntaList;
    }

    public void setPreguntaList(List<Pregunta> preguntaList) {
        this.preguntaList = preguntaList;
    }

    public List<RespuestaXExamen> getRespuestaXExamenList() {
        return respuestaXExamenList;
    }

    public void setRespuestaXExamenList(List<RespuestaXExamen> respuestaXExamenList) {
        this.respuestaXExamenList = respuestaXExamenList;
    }

    public Pregunta getIdPreguntaAsociada() {
        return idPreguntaAsociada;
    }

    public void setIdPreguntaAsociada(Pregunta idPreguntaAsociada) {
        this.idPreguntaAsociada = idPreguntaAsociada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOpcion != null ? idOpcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Opcion)) {
            return false;
        }
        Opcion other = (Opcion) object;
        if ((this.idOpcion == null && other.idOpcion != null) || (this.idOpcion != null && !this.idOpcion.equals(other.idOpcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Opcion[ idOpcion=" + idOpcion + " ]";
    }
    
}
