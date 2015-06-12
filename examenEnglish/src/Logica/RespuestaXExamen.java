/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author cristian
 */
@Entity
@Table(name = "respuesta_x_examen")
@NamedQueries({
    @NamedQuery(name = "RespuestaXExamen.findAll", query = "SELECT r FROM RespuestaXExamen r"),
    @NamedQuery(name = "RespuestaXExamen.findByIdRespuestaXExamen", query = "SELECT r FROM RespuestaXExamen r WHERE r.idRespuestaXExamen = :idRespuestaXExamen")})
public class RespuestaXExamen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_respuesta_x_examen")
    private Integer idRespuestaXExamen;
    @JoinColumn(name = "id_pregunta_referenciada", referencedColumnName = "id_pregunta")
    @ManyToOne(optional = false)
    private Pregunta idPreguntaReferenciada;
    @JoinColumn(name = "id_opcion_seleccionada", referencedColumnName = "id_opcion")
    @ManyToOne(optional = false)
    private Opcion idOpcionSeleccionada;
    @JoinColumn(name = "id_examen_referenciado", referencedColumnName = "id_examen")
    @ManyToOne(optional = false)
    private Examen idExamenReferenciado;

    public RespuestaXExamen() {
    }

    public RespuestaXExamen(Integer idRespuestaXExamen) {
        this.idRespuestaXExamen = idRespuestaXExamen;
    }

    public Integer getIdRespuestaXExamen() {
        return idRespuestaXExamen;
    }

    public void setIdRespuestaXExamen(Integer idRespuestaXExamen) {
        this.idRespuestaXExamen = idRespuestaXExamen;
    }

    public Pregunta getIdPreguntaReferenciada() {
        return idPreguntaReferenciada;
    }

    public void setIdPreguntaReferenciada(Pregunta idPreguntaReferenciada) {
        this.idPreguntaReferenciada = idPreguntaReferenciada;
    }

    public Opcion getIdOpcionSeleccionada() {
        return idOpcionSeleccionada;
    }

    public void setIdOpcionSeleccionada(Opcion idOpcionSeleccionada) {
        this.idOpcionSeleccionada = idOpcionSeleccionada;
    }

    public Examen getIdExamenReferenciado() {
        return idExamenReferenciado;
    }

    public void setIdExamenReferenciado(Examen idExamenReferenciado) {
        this.idExamenReferenciado = idExamenReferenciado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRespuestaXExamen != null ? idRespuestaXExamen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaXExamen)) {
            return false;
        }
        RespuestaXExamen other = (RespuestaXExamen) object;
        if ((this.idRespuestaXExamen == null && other.idRespuestaXExamen != null) || (this.idRespuestaXExamen != null && !this.idRespuestaXExamen.equals(other.idRespuestaXExamen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.RespuestaXExamen[ idRespuestaXExamen=" + idRespuestaXExamen + " ]";
    }
    
}
