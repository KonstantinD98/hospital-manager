package com.example.application.views.doctor;


import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Hospital;

import com.example.application.data.entity.Type;
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

public class DoctorForm extends FormLayout {
  private Doctor doctor;

  TextField firstName = new TextField("First name");
  TextField lastName = new TextField("Last name");
  EmailField email = new EmailField("Email");
  ComboBox<Type> type = new ComboBox<>("Type");
  ComboBox<Hospital> hospital = new ComboBox<>("hospital");
  Binder<Doctor> binder = new BeanValidationBinder<>(Doctor.class);

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  public DoctorForm(List<Hospital> hospitals, List<Type> types) {
    addClassName("doctor-form");
    binder.bindInstanceFields(this);

    hospital.setItems(hospitals);
    hospital.setItemLabelGenerator(Hospital::getName);
    type.setItems(types);
    type.setItemLabelGenerator(Type::getName);
    add(firstName,
        lastName,
        email,
        hospital,
        type,
        createButtonsLayout()); 
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, doctor)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));


    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close); 
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
    binder.readBean(doctor);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(doctor);
      fireEvent(new SaveEvent(this, doctor));
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }


  // Events
  public static abstract class DoctorFormEvent extends ComponentEvent<DoctorForm> {
    private Doctor doctor;

    protected DoctorFormEvent(DoctorForm source, Doctor doctor) {
      super(source, false);
      this.doctor = doctor;
    }

    public Doctor getDoctor() {
      return doctor;
    }
  }

  public static class SaveEvent extends DoctorFormEvent {
    SaveEvent(DoctorForm source, Doctor doctor) {
      super(source, doctor);
    }
  }

  public static class DeleteEvent extends DoctorFormEvent {
    DeleteEvent(DoctorForm source, Doctor doctor) {
      super(source, doctor);
    }

  }

  public static class CloseEvent extends DoctorFormEvent {
    CloseEvent(DoctorForm source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}