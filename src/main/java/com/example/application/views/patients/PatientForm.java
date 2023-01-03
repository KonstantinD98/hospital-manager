package com.example.application.views.patients;

import com.example.application.data.entity.Doctor;

import com.example.application.data.entity.Patient;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class PatientForm extends FormLayout {
    private Patient patient;

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    ComboBox<Doctor> doctor = new ComboBox<>("doctor");
    Binder<Patient> binder = new BeanValidationBinder<>(Patient.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public PatientForm(List<Doctor> doctors) {
        addClassName("patient-form");
        binder.bindInstanceFields(this);

        doctor.setItems(doctors);
        doctor.setItemLabelGenerator(Doctor::getFirstName);
        doctor.setItemLabelGenerator(Doctor::getLastName);


        add(firstName,
                lastName,
                email,
                doctor,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PatientForm.DeleteEvent(this, patient)));
        close.addClickListener(event -> fireEvent(new PatientForm.CloseEvent(this)));


        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        binder.readBean(patient);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(patient);
            fireEvent(new PatientForm.SaveEvent(this, patient));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
            // Events
            public static abstract class PatientFormEvent extends ComponentEvent<PatientForm> {
                private Patient patient;

                protected PatientFormEvent(PatientForm source, Patient patient) {
                    super(source, false);
                    this.patient = patient;
                }

                public Patient getPatient() {
                    return patient;
                }
            }

            public static class SaveEvent extends PatientForm.PatientFormEvent{
                SaveEvent(PatientForm source, Patient patient) {
                    super(source, patient);
                }
            }

            public static class DeleteEvent extends PatientForm.PatientFormEvent {
                DeleteEvent(PatientForm source, Patient patient) {
                    super(source, patient);
                }

            }

            public static class CloseEvent extends PatientForm.PatientFormEvent {
                CloseEvent(PatientForm source) {
                    super(source, null);
                }
            }

            public <T extends ComponentEvent<?>>Registration addListener (Class < T > eventType,
                    ComponentEventListener< T > listener){
                return getEventBus().addListener(eventType, listener);
            }

        }






