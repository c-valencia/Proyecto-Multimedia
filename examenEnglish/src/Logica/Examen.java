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
@Table(name = "examen")
@NamedQueries({
    @NamedQuery(name = "Examen.findAll", query = "SELECT e FROM Examen e"),
    @NamedQuery(name = "Examen.findByIdExamen", query = "SELECT e FROM Examen e WHERE e.idExamen = :idExamen"),
    @NamedQuery(name = "Examen.findByNotaObtenida", query = "SELECT e FROM Examen e WHERE e.notaObtenida = :notaObtenida")})
public class Examen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_examen")
    private Integer idExamen;
    @Basic(optional = false)
    @Column(name = "nota_obtenida")
    private double notaObtenida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idExamenReferenciado")
    private List<RespuestaXExamen> respuestaXExamenList;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Persona idEstudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idExamenReferenciado")
    private List<ExamenXPregunta> examenXPreguntaList;

    public Examen() {
    }

    public Examen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public Examen(Integer idExamen, double notaObtenida) {
        this.idExamen = idExamen;
        this.notaObtenida = notaObtenida;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public double getNotaObtenida() {
        return notaObtenida;
    }

    public void setNotaObtenida(double notaObtenida) {
        this.notaObtenida = notaObtenida;
    }

    public List<RespuestaXExamen> getRespuestaXExamenList() {
        return respuestaXExamenList;
    }

    public void setRespuestaXExamenList(List<RespuestaXExamen> respuestaXExamenList) {
        this.respuestaXExamenList = respuestaXExamenList;
    }

    public Persona getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Persona idEstudiante) {
        this.idEstudiante = idEstudiante;
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
        hash += (idExamen != null ? idExamen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Examen)) {
            return false;
        }
        Examen other = (Examen) object;
        if ((this.idExamen == null && other.idExamen != null) || (this.idExamen != null && !this.idExamen.equals(other.idExamen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Examen[ idExamen=" + idExamen + " ]";
    }
    
}
