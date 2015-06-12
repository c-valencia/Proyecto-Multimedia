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
@Table(name = "examen_x_pregunta")
@NamedQueries({
    @NamedQuery(name = "ExamenXPregunta.findAll", query = "SELECT e FROM ExamenXPregunta e"),
    @NamedQuery(name = "ExamenXPregunta.findByIdExamenXPregunta", query = "SELECT e FROM ExamenXPregunta e WHERE e.idExamenXPregunta = :idExamenXPregunta")})
public class ExamenXPregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_examen_x_pregunta")
    private Integer idExamenXPregunta;
    @JoinColumn(name = "id_pregunta_referenciada", referencedColumnName = "id_pregunta")
    @ManyToOne(optional = false)
    private Pregunta idPreguntaReferenciada;
    @JoinColumn(name = "id_examen_referenciado", referencedColumnName = "id_examen")
    @ManyToOne(optional = false)
    private Examen idExamenReferenciado;

    public ExamenXPregunta() {
    }

    public ExamenXPregunta(Integer idExamenXPregunta) {
        this.idExamenXPregunta = idExamenXPregunta;
    }

    public Integer getIdExamenXPregunta() {
        return idExamenXPregunta;
    }

    public void setIdExamenXPregunta(Integer idExamenXPregunta) {
        this.idExamenXPregunta = idExamenXPregunta;
    }

    public Pregunta getIdPreguntaReferenciada() {
        return idPreguntaReferenciada;
    }

    public void setIdPreguntaReferenciada(Pregunta idPreguntaReferenciada) {
        this.idPreguntaReferenciada = idPreguntaReferenciada;
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
        hash += (idExamenXPregunta != null ? idExamenXPregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamenXPregunta)) {
            return false;
        }
        ExamenXPregunta other = (ExamenXPregunta) object;
        if ((this.idExamenXPregunta == null && other.idExamenXPregunta != null) || (this.idExamenXPregunta != null && !this.idExamenXPregunta.equals(other.idExamenXPregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.ExamenXPregunta[ idExamenXPregunta=" + idExamenXPregunta + " ]";
    }
    
}
