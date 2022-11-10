package org.dis.front;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;
import org.dis.back.TipoEmpleado;
import org.w3c.dom.Text;

import java.util.Arrays;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    //crea un objeto de tipo label y le pone un nombre
    private TextField creaLabel(String texto){
        TextField etiqueta = new TextField();
        etiqueta.setCaption(texto);
        return etiqueta;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout salarioBruto = new HorizontalLayout();
        final HorizontalLayout salarioNeto = new HorizontalLayout();
        final VerticalLayout salarioBrutoContenedor = new VerticalLayout();
        final VerticalLayout salarioNetoContenedor = new VerticalLayout();

        //TextField tipo = creaLabel("Tipo de empleado");

        ComboBox <String> tipoEmpleadoComboBox = new ComboBox<>("Tipo de empleado", Arrays.asList(TipoEmpleado.ENCARGADO, TipoEmpleado.VENDEDOR));
        TextField ventasMes = creaLabel("Ventas del mes");
        TextField horasExtra = creaLabel("Nº de horas extra");

        TextField salarioBrutoLabel = creaLabel("Salario Bruto");

        salarioBruto.addComponents(tipoEmpleadoComboBox,ventasMes,horasExtra);
        salarioNeto.addComponent(salarioBrutoLabel);
        Button botonSalarioBruto = new Button("calcular Salario Bruto");
        botonSalarioBruto.addClickListener(e -> {
            String tipoEmpleadoIn = tipoEmpleadoComboBox.getValue();
            double ventasMesIn = Double.parseDouble(ventasMes.getValue());
            double horasExtrasIn = Double.parseDouble(horasExtra.getValue());

            EmpleadoBR empleado = new EmpleadoBR();
            try {
                double resultado = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventasMesIn, horasExtrasIn);
                Label labelSalarioBruto = new Label("El salario bruto obtenido es "+ resultado + "€");
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            } catch (BRException ex) {
                Label labelSalarioBruto = new Label(ex.getMessage());
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            }
        });

        Button botonSalarioNeto = new Button("calcular Salario Neto");
        botonSalarioNeto.addClickListener(e -> {
            double salarioBrutoIn = Double.parseDouble(salarioBrutoLabel.getValue());

            EmpleadoBR empleado = new EmpleadoBR();
            try {
                double resultado = empleado.calculaSalarioNeto(salarioBrutoIn);
                Label labelSalarioNeto = new Label("El salario Neto obtenido es "+ resultado + "€");
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            } catch (BRException ex) {
                Label labelSalarioNeto = new Label(ex.getMessage());
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            }
        });

        salarioBrutoContenedor.addComponents(salarioBruto,botonSalarioBruto);
        salarioNetoContenedor.addComponents(salarioNeto, botonSalarioNeto);

        TabSheet tabs = new TabSheet();
        tabs.addTab(salarioBrutoContenedor).setCaption("Calcula Salario Bruto");
        tabs.addTab(salarioNetoContenedor).setCaption("Calcula Salario Neto");

        layout.addComponent(tabs);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
