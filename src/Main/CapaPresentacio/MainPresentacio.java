package Main.CapaPresentacio;

public class MainPresentacio {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
                ctrlPresentacio.inicialitzarPresentacio();
            }
        });
    }
}
