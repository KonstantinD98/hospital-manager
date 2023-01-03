package com.example.application.views.doctor;


import com.example.application.data.entity.Doctor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DoctorViewTest {

    @Autowired
    private DoctorView DoctorView;

    @Test
    public void formShownWhenDoctorSelected() {
        Grid<Doctor> grid = DoctorView.grid;
        Doctor firstDoctor = getFirstItem(grid);

        DoctorForm form = DoctorView.form;

        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstDoctor);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstDoctor.getFirstName(), form.firstName.getValue());
    }
    private Doctor getFirstItem(Grid<Doctor> grid) {
        return( (ListDataProvider<Doctor>) grid.getDataProvider()).getItems().iterator().next();
    }
}