package controle;

import exception.ConversaoException;
import exception.GerarPlanilhaException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class ExcelUtil {
    public File gerarPlanilha(List<String> colunasCabecalho, List<List<String>> infos) throws ConversaoException, GerarPlanilhaException {
        String path = System.getProperty("user.home");
        if ((new File(path + "\\Downloads")).exists()) {
            path = path + "\\Downloads";
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        String nomeSheet = ConversorUtil.getDateToString(Calendar.getInstance());
        Sheet sheet = workbook.createSheet(nomeSheet);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        for (String colunaCabecalho: colunasCabecalho) {
            Cell headerCell = header.createCell(colunasCabecalho.indexOf(colunaCabecalho));
            headerCell.setCellValue(colunaCabecalho);
            headerCell.setCellStyle(headerStyle);
        }

        int linhas = 0;
        for (List<String> info: infos) {
            linhas = linhas + 1;
            Row linha = sheet.createRow(linhas);

            for (int i = 0; i < info.size(); i++) {
                Cell cell = linha.createCell(i);
                cell.setCellValue(info.get(i));
                cell.setCellStyle(style);

                System.out.println("VALOR RECEBIDO " + info.get(i));
                System.out.println("VALOR GUARDADO " + cell.getStringCellValue());
            }
        }
        
        File arquivo = new File(path + "\\" + nomeSheet + ".xlsx");
        try {
            FileOutputStream outputStream = new FileOutputStream(arquivo);
            workbook.write(outputStream);
            workbook.close();
        }
        catch (Exception exception){
            throw new GerarPlanilhaException();
        }

        return arquivo;
    }
}
