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
@Table(name = "pregunta")
@NamedQueries({
    @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p"),
    @NamedQuery(name = "Pregunta.findByIdPregunta", query = "SELECT p FROM Pregunta p WHERE p.idPregunta = :idPregunta"),
    @NamedQuery(name = "Pregunta.findByEnunciado", query = "SELECT p FROM Pregunta p WHERE p.enunciado = :enunciado"),
    @NamedQuery(name = "Pregunta.findByMaterialMultiemdia", query = "SELECT p FROM Pregunta p WHERE p.materialMultiemdia = :materialMultiemdia")})
public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pregunta")
    private Integer idPregunta;
    @Basic(optional = false)
    @Column(name = "enunciado")
    private String enunciado;
    @Basic(optional = false)
    @Column(name = "material_multiemdia")
    private String materialMultiemdia;
    @JoinColumn(name = "id_opcion_correcta", referencedColumnName = "id_opcion")
    @ManyToOne(optional = false)
    private Opcion idOpcionCorrecta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPreguntaReferenciada")
    private List<RespuestaXExamen> respuestaXExamenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPreguntaAsociada")
    private List<Opcion> opcionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPreguntaReferenciada")
    private List<ExamenXPregunta> examenXPreguntaList;

    public Pregunta() {
    }

    public Pregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public Pregunta(Integer idPregunta, String enunciado, String materialMultiemdia) {
        this.idPregunta = idPregunta;
        this.enunciado = enunciado;
        this.materialMultiemdia = materialMultiemdia;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getMaterialMultiemdia() {
        return materialMultiemdia;
    }

    public void setMaterialMultiemdia(String materialMultiemdia) {
        this.materialMultiemdia = materialMultiemdia;
    }

    public Opcion getIdOpcionCorrecta() {
        return idOpcionCorrecta;
    }

    public void setIdOpcionCorrecta(Opcion idOpcionCorrecta) {
        this.idOpcionCorrecta = idOpcionCorrecta;
    }

    public List<RespuestaXExamen> getRespuestaXExamenList() {
        return respuestaXExamenList;
    }

    public void setRespuestaXExamenList(List<RespuestaXExamen> respuestaXExamenList) {
        this.respuestaXExamenList = respuestaXExamenList;
    }

    public List<Opcion> getOpcionList() {
        return opcionList;
    }

    public void setOpcionList(List<Opcion> opcionList) {
        this.opcionList = opcionList;
    }

    public List<ExamenXPregunta> getExamenXPreguntaList() {
        return examenXPreguntaList;
    }

    public void setExamenXPreguntaList(List<ExamenXPregunta> examenXPreguntaList) {
        this.examenXPreguntaList = examenXPreguntaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPregunta != null ? idPregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        if ((this.idPregunta == null && other.idPregunta != null) || (this.idPregunta != null && !this.idPregunta.equals(other.idPregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Pregunta[ idPregunta=" + idPregunta + " ]";
    }
    
}
