package com.example.application.views.doctor;


import com.example.application.data.entity.Doctor;

import com.example.application.data.service.HospitalService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;


@Component
@Scope("prototype")
@Route(value="", layout = MainLayout.class)
@PageTitle("Doctors | Hospital Manager")
@PermitAll
public class DoctorView extends VerticalLayout {
    Grid<Doctor> grid = new Grid<>(Doctor.class);
    TextField filterText = new TextField();
    DoctorForm form;
    HospitalService service;


    public DoctorView(HospitalService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new DoctorForm(service.findAllHospitals(), service.findAllTypes());
        form.setWidth("25em");
        form.addListener(DoctorForm.SaveEvent.class, this::saveDoctor);
        form.addListener(DoctorForm.DeleteEvent.class, this::deleteDoctor);
        form.addListener(DoctorForm.CloseEvent.class, e -> closeEditor());

        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event ->
            editDoctor(event.getValue()));
    }

    private void configureGrid() {
        grid.addClassNames("doctor-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(doctor -> doctor.getType().getName()).setHeader("Type");
        grid.addColumn(doctor -> doctor.getHospital().getName()).setHeader("Hospital");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addDoctorButton = new Button("Add doctor");
        addDoctorButton.addClickListener(click -> addDoctor());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addDoctorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void saveDoctor(DoctorForm.SaveEvent event) {
        service.saveDoctor(event.getDoctor());
        updateList();
        closeEditor();
    }

    private void deleteDoctor(DoctorForm.DeleteEvent event) {
        service.deleteDoctor(event.getDoctor());
        updateList();
        closeEditor();
    }

    public void editDoctor(Doctor doctor) {
        if (doctor == null) {
            closeEditor();
        } else {
            form.setDoctor(doctor);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addDoctor() {
        grid.asSingleSelect().clear();
        editDoctor(new Doctor());
    }

    private void closeEditor() {
        form.setDoctor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllDoctors(filterText.getValue()));
    }


}
