package com.dekwin.autorep.entities;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by dekst on 10.11.2015.
 */
public class Contract {
    private int id;
    private Responsible responsible;
    private Organization organization;
    private GregorianCalendar initialDate;
    private GregorianCalendar finalDate;

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible= responsible;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization org) {
        this.organization = org;
    }

    public void setInitialDate(GregorianCalendar gc){
        this.initialDate=gc;

    }

    public GregorianCalendar getInitialDate(){
        return initialDate;
    }

    public void setFinalDate(GregorianCalendar gc){
        this.finalDate=gc;

    }

    public GregorianCalendar getFinalDate(){
        return finalDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contract(int id, Responsible resp, Organization org, GregorianCalendar initialDate, GregorianCalendar finalDate) {

        this.id = id;
        this.responsible=resp;
        this.organization=org;
        this.initialDate = initialDate;
        this.finalDate=finalDate;
    }
}
