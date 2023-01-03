package com.example.application.views.patients;

import com.example.application.data.entity.Patient;
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
    @Route(value="patients", layout = MainLayout.class)
    @PageTitle("Patient | Vaadin CRM")
    @PermitAll
    public class PatientView extends VerticalLayout {
        Grid<Patient> grid = new Grid<>(Patient.class);
        TextField filterText = new TextField();
        PatientForm form;
        HospitalService service;

        public PatientView(HospitalService service) {
            this.service = service;
            addClassName("patient-view");
            setSizeFull();
            configureGrid();

            form = new PatientForm(service.findAllDoctors());
            form.setWidth("25em");
            form.addListener(PatientForm.SaveEvent.class, this::savePatient);
            form.addListener(PatientForm.DeleteEvent.class, this::deletePatient);
            form.addListener(PatientForm.CloseEvent.class, e -> closeEditor());
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
                    editPatient(event.getValue()));
        }
            private void configureGrid() {
                grid.addClassNames("patient-grid");
                grid.setSizeFull();
                grid.setColumns("firstName", "lastName", "email");
                grid.addColumn(patient -> patient.getDoctor().getFirstName()).setHeader("Doctor");
                grid.getColumns().forEach(col -> col.setAutoWidth(true));
            }
            private HorizontalLayout getToolbar() {
                filterText.setPlaceholder("Filter by name...");
                filterText.setClearButtonVisible(true);
                filterText.setValueChangeMode(ValueChangeMode.LAZY);
                filterText.addValueChangeListener(e -> updateList());

                Button addPatientButton = new Button("Add Patient");
                addPatientButton.addClickListener(click -> addPatient());

                HorizontalLayout toolbar = new HorizontalLayout(filterText, addPatientButton);
                toolbar.addClassName("toolbar");
                return toolbar;
            }
            private void savePatient(PatientForm.SaveEvent event) {
                service.savePatient(event.getPatient());
                updateList();
                closeEditor();
            }

            private void deletePatient(PatientForm.DeleteEvent event) {
                service.deletePatient(event.getPatient());
                updateList();
                closeEditor();
            }

            public void editPatient(Patient patient) {
                if (patient == null) {
                    closeEditor();
                } else {
                    form.setPatient(patient);
                    form.setVisible(true);
                    addClassName("editing");
                }
            }

            void addPatient() {
                grid.asSingleSelect().clear();
                editPatient(new Patient());
            }

            private void closeEditor() {
                form.setPatient(null);
                form.setVisible(false);
                removeClassName("editing");
            }

            private void updateList() {
                grid.setItems(service.findAllPatients(filterText.getValue()));
            }
        }

