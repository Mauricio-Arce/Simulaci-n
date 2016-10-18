package graficobastones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class VentanaPrincipal extends javax.swing.JFrame {
    private String direccionArchivo;

    public VentanaPrincipal() {
        initComponents();
        this.jPanelGrafico.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooiser = new javax.swing.JFileChooser();
        jPanelGrafico = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jBtnEjecutar = new javax.swing.JButton();
        jRbtnContinuas = new javax.swing.JRadioButton();
        jRbtnDiscretas = new javax.swing.JRadioButton();
        jBtnBuscadorArchivo = new javax.swing.JButton();
        jTxtArchivo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTxtAFo = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelGrafico.setBackground(java.awt.Color.white);

        javax.swing.GroupLayout jPanelGraficoLayout = new javax.swing.GroupLayout(jPanelGrafico);
        jPanelGrafico.setLayout(jPanelGraficoLayout);
        jPanelGraficoLayout.setHorizontalGroup(
            jPanelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1120, Short.MAX_VALUE)
        );
        jPanelGraficoLayout.setVerticalGroup(
            jPanelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(58, 93, 108));

        jBtnEjecutar.setBackground(new java.awt.Color(191, 84, 76));
        jBtnEjecutar.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jBtnEjecutar.setForeground(java.awt.Color.white);
        jBtnEjecutar.setText("Graficar");
        jBtnEjecutar.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jBtnEjecutar.setFocusPainted(false);
        jBtnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEjecutarActionPerformed(evt);
            }
        });

        jRbtnContinuas.setBackground(new java.awt.Color(58, 93, 108));
        jRbtnContinuas.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jRbtnContinuas.setForeground(java.awt.Color.white);
        jRbtnContinuas.setText("Continuas");
        jRbtnContinuas.setFocusPainted(false);
        jRbtnContinuas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRbtnContinuasActionPerformed(evt);
            }
        });

        jRbtnDiscretas.setBackground(new java.awt.Color(58, 93, 108));
        jRbtnDiscretas.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jRbtnDiscretas.setForeground(java.awt.Color.white);
        jRbtnDiscretas.setText("Discretas");
        jRbtnDiscretas.setFocusPainted(false);
        jRbtnDiscretas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRbtnDiscretasActionPerformed(evt);
            }
        });

        jBtnBuscadorArchivo.setBackground(new java.awt.Color(191, 84, 76));
        jBtnBuscadorArchivo.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jBtnBuscadorArchivo.setForeground(java.awt.Color.white);
        jBtnBuscadorArchivo.setText("Buscar");
        jBtnBuscadorArchivo.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jBtnBuscadorArchivo.setFocusPainted(false);
        jBtnBuscadorArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscadorArchivoActionPerformed(evt);
            }
        });

        jTxtArchivo.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Archivo");

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VARIABLES ALEATORIAS");

        jTxtAFo.setEditable(false);
        jTxtAFo.setColumns(20);
        jTxtAFo.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jTxtAFo.setRows(5);
        jScrollPane1.setViewportView(jTxtAFo);

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Frecuencias Observadas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBtnEjecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jRbtnDiscretas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRbtnContinuas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBtnBuscadorArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtArchivo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1))))
                .addGap(27, 27, 27))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTxtArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnBuscadorArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRbtnContinuas, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRbtnDiscretas, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jBtnEjecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelGrafico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRbtnDiscretasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRbtnDiscretasActionPerformed
        jRbtnContinuas.setSelected(false);
    }//GEN-LAST:event_jRbtnDiscretasActionPerformed

    private void jRbtnContinuasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRbtnContinuasActionPerformed
        jRbtnDiscretas.setSelected(false);
    }//GEN-LAST:event_jRbtnContinuasActionPerformed

    private void jBtnBuscadorArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscadorArchivoActionPerformed
        File workingDirectory = new File(System.getProperty("user.dir").replace("/GraficoBastones", ""));
        JFileChooser buscador = new JFileChooser(); 
        buscador.setCurrentDirectory(workingDirectory);
        FileNameExtensionFilter filtrarResultados = new FileNameExtensionFilter("TXT","txt");
        buscador.setFileFilter(filtrarResultados);
        int ventana = buscador.showOpenDialog(jFileChooiser);
       
        if(ventana == JFileChooser.APPROVE_OPTION){
            jTxtArchivo.setText(buscador.getSelectedFile().getName());
            this.direccionArchivo = buscador.getCurrentDirectory() + "/" +buscador.getSelectedFile().getName(); 
        } 
        
    }//GEN-LAST:event_jBtnBuscadorArchivoActionPerformed

    private void jBtnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEjecutarActionPerformed
        if(!jTxtArchivo.getText().equals("") && (jRbtnDiscretas.isSelected() || jRbtnContinuas.isSelected())){
            try {     
                GenerarGrafica grafica = new GenerarGrafica(direccionArchivo,jRbtnDiscretas.isSelected());
            
                if(jRbtnDiscretas.isSelected()){
                    grafica.leerArchivo();
                    graficaDiscreta(grafica.getResultadoVariables(), grafica.getInicio(), grafica.getFin());
                    mostrarFrecuencias(grafica.getResultadoVariables());
                }
                else if(jRbtnContinuas.isSelected()){
                    grafica.leerArchivo();
                    //graficaContinuas(grafica.getVariablesContinuas());
                    graficaDiscreta(grafica.getVariablesContinuas(),grafica.getInicio(),grafica.getFin());
                    mostrarFrecuencias(grafica.getVariablesContinuas());
                }
                
                
            
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBtnEjecutarActionPerformed
      
    public void graficaDiscreta(ArrayList<ArrayList> resultados, int inicio, int fin){
        DefaultCategoryDataset datos = new DefaultCategoryDataset(); 
        
        for(int i = 0; i < resultados.size(); i++){
            datos.addValue((Number) resultados.get(i).get(1),"Variables Aleatorias",String.valueOf(resultados.get(i).get(0)));
        }
        JFreeChart grafica = ChartFactory.createBarChart3D("","Rango","Apariciones",datos, PlotOrientation.VERTICAL,true,true,true);
        CategoryPlot plot = (CategoryPlot) grafica.getPlot();
        plot.setDomainGridlinesVisible(true);
        
        ChartPanel panel = new ChartPanel(grafica);
        panel.setBounds(5,10,900,763);
        
        jPanelGrafico.removeAll();
        jPanelGrafico.add(panel,BorderLayout.CENTER);
        jPanelGrafico.repaint();
    }   
    public void graficaContinuas(ArrayList<ArrayList> resultadoVariables) {
        XYSplineRenderer renderer = new XYSplineRenderer();
        XYSeriesCollection datos = new XYSeriesCollection();
        
        ValueAxis x = new NumberAxis();
        ValueAxis y = new NumberAxis();
        
        XYSeries serie = new XYSeries("Variables Aleatorias");
        XYPlot plot;
        
        for(int i = 0; i < resultadoVariables.size(); i++){
            float ejeX = (float) resultadoVariables.get(i).get(0);
            serie.add(ejeX, (int) resultadoVariables.get(i).get(1));
        }
        
        datos.addSeries(serie);
        x.setLabel("Rango");
        y.setLabel("Apariciones");
        plot = new XYPlot(datos,x,y,renderer);
        
        JFreeChart grafica = new JFreeChart(plot);
        
        ChartPanel panel = new ChartPanel(grafica);
        panel.setBounds(5,10,900,763);
        
        jPanelGrafico.removeAll();
        jPanelGrafico.add(panel,BorderLayout.CENTER);
        jPanelGrafico.repaint();
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnBuscadorArchivo;
    private javax.swing.JButton jBtnEjecutar;
    private javax.swing.JFileChooser jFileChooiser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelGrafico;
    private javax.swing.JRadioButton jRbtnContinuas;
    private javax.swing.JRadioButton jRbtnDiscretas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTxtAFo;
    private javax.swing.JTextField jTxtArchivo;
    // End of variables declaration//GEN-END:variables

    private void mostrarFrecuencias(ArrayList<ArrayList> resultadoVariables) {
        String resultado = "";
        resultado += "X\tOcurrencias\n";
        for(int i = 0; i < resultadoVariables.size(); i++){
            resultado += resultadoVariables.get(i).get(0) + "\t" + resultadoVariables.get(i).get(1) +"\n";
        }
        jTxtAFo.setText(resultado);
        System.out.println(resultadoVariables.size());
    }

}
