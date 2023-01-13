package Swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;

	private List<Integer> listaPosicaoComecoMotifResultanteMeme;
	private List<Integer> listaPosicaoComecoMotifResultanteBio;
	private List<String> listaDataSet;
	private List<String> listaDataSetBio;
	private List<String> listaMeme;
	private List<String> listaBio;
	private List<String> listaResultadoMeme;
	private List<String> listaResultadoBio;
	private List<String> listaStringResultanteReal;
	private List<String> listaStringResultanteMeme;
	private List<String> listaStringResultanteBio;
	private Path path;
	private File arquivoUpload;
	private File arquivoSelecionado;
	private Integer motifTamanho = 0;
	private Integer qtdeDeAnalise = 0;
	private Integer posicaoComecoMotifOriginal;
	private String pathnameUpload = null;
	private String nomeDataSet = null;
	private String linhaUpload = null;
	private String melhorResultado = null;
	private FileWriter fileWriterResult;
	private PrintWriter printWriterResult;
	private JTextField tamanhoMotif;
	private JTextField qtdeAnalise;

	public Tela() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Motif result lexical analyzer");
		setBounds(100, 100, 450, 204);
		getContentPane().setLayout(null);
		
		JPanel panelInformacoes = new JPanel();
		panelInformacoes.setBounds(10, 2, 414, 112);
		getContentPane().add(panelInformacoes);
		panelInformacoes.setBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), 
						"Reunindo Informa\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelInformacoes.setLayout(null);

		JButton btnUpload = new JButton("Fazer upload do dataset");
		btnUpload.setBackground(Color.LIGHT_GRAY);
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				int respostDoFileChooser = jFileChooser.showOpenDialog(null);
				if (respostDoFileChooser == JFileChooser.APPROVE_OPTION) {
					arquivoSelecionado = jFileChooser.getSelectedFile();

					btnUpload.setBackground(Color.GREEN);
					btnUpload.setText(arquivoSelecionado.getName());

					linhaUpload = new String();
					pathnameUpload = arquivoSelecionado.getAbsolutePath();
					nomeDataSet = arquivoSelecionado.getName();
					arquivoUpload = new File(pathnameUpload);
				}
			}
		});
		btnUpload.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpload.setBounds(20, 20, 370, 23);
		panelInformacoes.add(btnUpload);

		JLabel lblTamanhoMotif = new JLabel("Tamanho do motif do DataSet: ");
		lblTamanhoMotif.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTamanhoMotif.setBounds(22, 50, 253, 23);
		panelInformacoes.add(lblTamanhoMotif);

		tamanhoMotif = new JTextField();
		tamanhoMotif.setBounds(285, 50, 105, 23);
		panelInformacoes.add(tamanhoMotif);
		tamanhoMotif.setColumns(10);

		JLabel lblQtdeAnalise = new JLabel("Quantas análises dejesa fazer: ");
		lblQtdeAnalise.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblQtdeAnalise.setBounds(22, 80, 253, 23);
		panelInformacoes.add(lblQtdeAnalise);

		qtdeAnalise = new JTextField();
		qtdeAnalise.setColumns(10);
		qtdeAnalise.setBounds(285, 80, 105, 23);
		panelInformacoes.add(qtdeAnalise);

		JButton btnContinuar = new JButton("Continuar");
		btnContinuar.setBounds(120, 130, 185, 23);
		getContentPane().add(btnContinuar);
		btnContinuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Objects.isNull(arquivoSelecionado)) {
					JOptionPane.showMessageDialog(panelInformacoes, "É necessário selecionar o DataSet a ser analisado!");
					btnUpload.requestFocus();
					return;
				}
				
				if (tamanhoMotif.getText().isBlank()) {
					JOptionPane.showMessageDialog(panelInformacoes, "É necessário preencher o campo 'Tamanho do motif do DataSet'!");
					tamanhoMotif.requestFocus();
					return;
				}
				
				if (qtdeAnalise.getText().isBlank()) {
					JOptionPane.showMessageDialog(panelInformacoes, "É necessário preencher o campo 'Quantas análises deseja fazer'!");
					qtdeAnalise.requestFocus();
					return;
				}

				btnContinuar.setVisible(false);
				panelInformacoes.setVisible(false);
				motifTamanho = Integer.parseInt(tamanhoMotif.getText());
				qtdeDeAnalise = Integer.parseInt(qtdeAnalise.getText());
				File arquivo = new File("/home/caio_henrique/arquivo.sh");
				try {
					for (int i = 0; i < qtdeDeAnalise; i++) {
						if (!arquivo.exists()) {
							arquivo.createNewFile();
						}

						listaDataSet = new ArrayList<>();
						if (arquivoUpload.exists()) {
							try {
								FileReader leitorDeArquivo = new FileReader(pathnameUpload);
								BufferedReader bufferedReader = new BufferedReader(leitorDeArquivo);
								while (true) {
									linhaUpload = bufferedReader.readLine();
									if (Objects.isNull(linhaUpload)) {
										break;
									}

									listaDataSet.add(linhaUpload);
								}
							} catch (Exception exception) {
								throw new Exception("Problema ao importar o DataSet original");
							}
						}

						listaDataSetBio = new ArrayList<>();
						listaMeme = new ArrayList<>();
						listaBio = new ArrayList<>();
						listaResultadoMeme = new ArrayList<>();
						listaResultadoBio = new ArrayList<>();
						listaPosicaoComecoMotifResultanteMeme = new ArrayList<>();
						listaPosicaoComecoMotifResultanteBio = new ArrayList<>();
						processoMeme(arquivo, i);
						processoBioP(arquivo, i);
					}

					FileWriter fileWriter = new FileWriter(arquivo);
					PrintWriter printWriter = new PrintWriter(fileWriter);

					printWriter.println("rm /home/caio_henrique/DataSets/DataSetBio_" + nomeDataSet);
					printWriter.println("rm /home/caio_henrique/arquivo.sh");
					printWriter.flush();
					printWriter.close();

					Runtime.getRuntime().exec("sh /home/caio_henrique/arquivo.sh");
				} catch (Exception excep) {}
				
				if (Objects.isNull(arquivoSelecionado)) {
					JOptionPane.showMessageDialog(panelInformacoes, "Melhor Fscore foi do " + melhorResultado + " !");
					Integer condicao = JOptionPane.showConfirmDialog(null, "Deseja realizar um novo processo de análise?",
							"Confirmação...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (condicao == 0) {
						setVisible(false);
						Tela tela = new Tela();
						tela.setLocationRelativeTo(null);
						tela.setVisible(true);
					} else {
						System.exit(0);
					}
				}
			}
		});
		btnContinuar.setFont(new Font("Tahoma", Font.BOLD, 11));
	}

	private void processoMeme(File arquivo, Integer i) throws IOException, InterruptedException {
		ajustarDataSet();
		if (!arquivo.exists()) {
			arquivo.createNewFile();
		}

		FileWriter fileWriter = new FileWriter(arquivo);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println("cd /home/caio_henrique/Resultados");
		printWriter.println("mkdir saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')));
		printWriter.println(
				"cd /home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')));
		printWriter.println("mkdir Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1));
		printWriter.println(
				"cd /home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.'))
						+ "/Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1));
		printWriter.println("meme");
		printWriter.println("meme /home/caio_henrique/DataSets/" + nomeDataSet
				+ " -o saidaMeme -dna -mod oops -nmotifs 1 -w " + motifTamanho + " -brief 1000000");
		printWriter.flush();
		printWriter.close();

		Runtime run = Runtime.getRuntime();
		run.exec("sh /home/caio_henrique/arquivo.sh");
		path = Paths.get("/home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.'))
				+ "/Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1)
				+ "/saidaMeme/meme.html");
		
		do {} while (Files.notExists(path));
		
		ajustarMeme("/home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.'))
				+ "/Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1)
				+ "/saidaMeme/meme.txt");
		
		File arq = new File("/home/caio_henrique/Resultados/saida"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "/Analise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1) + "/resultadoAnalise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1) + ".txt");
		
		if (!arq.exists()) {
			arq.createNewFile();
		}
		
		fileWriterResult = new FileWriter(arq, false);
		printWriterResult = new PrintWriter(fileWriterResult);
		Integer falsoPositivo = 0;
		Integer falsoNegativo = 0;
		Integer verdadeiroPositivo = 0;
		Integer verdadeiroNegativo = 0;
		for (int j = 0; j < listaResultadoMeme.size(); j++) {
			for (int t = 0; t < listaDataSet.get(j).length(); t++) {
				char caracterLista1 = listaDataSet.get(j).charAt(t);
				char caracterLista2 = listaResultadoMeme.get(j).charAt(t);
				if (Character.isLowerCase(caracterLista1) && Character.isLowerCase(caracterLista2)) {
					falsoPositivo += 1;
				}
				
				if (Character.isUpperCase(caracterLista1) && Character.isUpperCase(caracterLista2)) {
					verdadeiroPositivo += 1;
				}
				
				if (Character.isLowerCase(caracterLista1) && Character.isUpperCase(caracterLista2)) {
					falsoNegativo += 1;
				}
				
				if (Character.isUpperCase(caracterLista1) && Character.isLowerCase(caracterLista2)) {
					verdadeiroNegativo += 1;
				}
			}
		}
		
		gerarResultadoAnalise("meme", falsoPositivo, falsoNegativo, verdadeiroPositivo, verdadeiroNegativo, null);

		printWriterResult.flush();
		printWriterResult.close();
	}

	private void ajustarDataSet() {
		// AJUSTANDO LISTA UPLOAD1 E EXCLUINDO A LINHA DE BAIXO QUE FOI CONCATENADA
		for (int i = 0; i < listaDataSet.size(); i++) {
			if (listaDataSet.get(i).charAt(0) != '>') {
				listaDataSet.set(i, listaDataSet.get(i).concat(listaDataSet.get(i + 1)));
				listaDataSet.remove(listaDataSet.get(i + 1));
				try {
					while (listaDataSet.get(i + 1).charAt(0) != '>') {
						listaDataSet.set(i, listaDataSet.get(i).concat(listaDataSet.get(i + 1)));
						listaDataSet.remove(listaDataSet.get(i + 1));
					}
				} catch (Exception e) {}
			}
		}
		
		criarDataSetBioProspector();
	}

	private void ajustarMeme(String pathname) {
		extrariResultadoMeme(pathname);

		// AJUSTANDO LISTA MEME
		for (int i = 0; i < listaMeme.size(); i++) {
			listaMeme.set(i, listaMeme.get(i).substring(0, 31));
		}

		// INSERINDO DADOS NA LISTA DE RESULTADO QUE SERÁ COMPARADA POSTERIORMENTE COM A
		// LISTA ORIGINAL
		for (int i = 0; i < listaDataSet.size(); i++) {
			for (int j = 0; j < listaMeme.size(); j++) {
				if (listaDataSet.get(i).contains(listaMeme.get(j).substring(0, 24))) {
					listaResultadoMeme.add(listaDataSet.get(i + 1));
					String pos = listaMeme.get(j).substring(28, 31).trim();
					listaPosicaoComecoMotifResultanteMeme.add(Integer.parseInt(pos) - 1);
					break;
				}
			}
		}

		// SEPARANDO CAPA PARTE QUE COMPÕE A SEQUÊNCIA DO DNA DO MOTIF RESULTANTE
		for (int i = 0; i < listaResultadoMeme.size(); i++) {
			String primeiraParte = listaResultadoMeme.get(i).substring(0, listaPosicaoComecoMotifResultanteMeme.get(i));
			String motifResultante = listaResultadoMeme.get(i).substring(listaPosicaoComecoMotifResultanteMeme.get(i),
					listaPosicaoComecoMotifResultanteMeme.get(i) + motifTamanho);
			String segundaParte = listaResultadoMeme.get(i).substring(
					listaPosicaoComecoMotifResultanteMeme.get(i) + motifTamanho, listaResultadoMeme.get(i).length());
			listaResultadoMeme.set(i,
					primeiraParte.toLowerCase() + motifResultante.toUpperCase() + segundaParte.toLowerCase());
		}
		
		// TRANSFORMANDO DATASET DE LETRAS PARA NÚMEROS PARA ANÁLISE EM R POSTERIORMENTE
		listaStringResultanteMeme = new ArrayList<>();
		for (int i = 0; i < listaResultadoMeme.size(); i++) {
			for (int j = 0; j < listaResultadoMeme.get(i).length(); j++) {
				if (Character.isUpperCase(listaResultadoMeme.get(i).charAt(j))) {
					listaStringResultanteMeme.add("1");
				} else {
					listaStringResultanteMeme.add("0");
				}
			}
		}

		finalizandoAjusteDataSet();
	}

	private void extrariResultadoMeme(String pathname) {
		// MÉTODO PARA EXTRAÇÃO DA PARTE QUE INTERESSA NO RESULTADO
		int cont = 0;
		String linha = new String();
		File arquivo = new File(pathname);
		if (arquivo.exists()) {
			try {
				FileReader leitorDeArquivo = new FileReader(arquivo);
				BufferedReader bufferedReader = new BufferedReader(leitorDeArquivo);
				while (true) {
					linha = bufferedReader.readLine();
					if (Objects.isNull(linha)) {
						break;
					}
					
					String validacaoInicio = "Sequence name             Start";
					while (cont == 0) {
						linha = bufferedReader.readLine();
						if (linha.startsWith(validacaoInicio)) {
							linha = bufferedReader.readLine();
							linha = bufferedReader.readLine();
							cont++;
						}
					}
					
					String validaFinal = "--------------------------------------------------------------------------------";
					if (!linha.startsWith(validaFinal)) {
						listaMeme.add(linha);
					} else {
						break;
					}
				}
			} catch (Exception exception) {}
		}
	}

	private void finalizandoAjusteDataSet() {
		// REMOVENDO LINHA QUE NÃO É A SEQUÊNCIA DE DNA
		for (int i = 0; i < listaDataSet.size(); i++) {
			if (listaDataSet.get(i).charAt(0) == '>') {
				listaDataSet.remove(i);
			}
		}
		
		listaStringResultanteReal = new ArrayList<>();
		for (int i = 0; i < listaDataSet.size(); i++) {
			for (int j = 0; j < listaDataSet.get(i).length(); j++) {
				if (Character.isUpperCase(listaDataSet.get(i).charAt(j))) {
					listaStringResultanteReal.add("1");
				} else {
					listaStringResultanteReal.add("0");
				}
			}
		}

		// PEGANDO A POSIÇÃO ONDE COMEÇA O MOTIF ORIGINAL
		for (int i = 0; i < listaDataSet.get(1).length(); i++) {
			if (Character.isUpperCase(listaDataSet.get(1).charAt(i))) {
				posicaoComecoMotifOriginal = i;
				break;
			}
		}

		// SEPARANDO CADA PARTE QUE COMPÕE A SEQUÊNCIA DO DNA DO MOTIF ORIGINAL
		for (int i = 0; i < listaDataSet.size(); i++) {
			String primeiraParte = listaDataSet.get(i).substring(0, posicaoComecoMotifOriginal);
			String motifOriginal = listaDataSet.get(i).substring(posicaoComecoMotifOriginal,
					posicaoComecoMotifOriginal + motifTamanho);
			String segundaParte = listaDataSet.get(i).substring(posicaoComecoMotifOriginal + motifTamanho,
					listaDataSet.get(i).length());
			listaDataSet.set(i, primeiraParte + motifOriginal + segundaParte);
		}
	}

	private void processoBioP(File arquivo, int i) throws IOException, InterruptedException {
		FileWriter fileWriter = new FileWriter(arquivo);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println("cd /home/caio_henrique/Resultados");
		printWriter.println(
				"cd /home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')));
		printWriter.println(
				"cd /home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.'))
						+ "/Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1));
		printWriter.println("mkdir saidaBioP.");
		printWriter.println("cd /home/caio_henrique/Resultados/saida"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "/Analise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1) + "/saidaBioP.");
		printWriter.println("bioprospector");
		printWriter.println("bioprospector -i /home/caio_henrique/DataSets/DataSetBio_" + nomeDataSet + " -W "
				+ motifTamanho + " -o bioprospector.txt -a 1 -r 1 -d 1");
		printWriter.flush();
		printWriter.close();

		Runtime.getRuntime().exec("sh /home/caio_henrique/arquivo.sh");
		Thread.sleep(15000);

		ajustarBioProspector(
				"/home/caio_henrique/Resultados/saida" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.'))
						+ "/Analise" + nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1)
						+ "/saidaBioP./bioprospector.txt");

		File arq = new File("/home/caio_henrique/Resultados/saida"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "/Analise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1) + "/resultadoAnalise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1) + ".txt");

		fileWriterResult = new FileWriter(arq, true);
		printWriterResult = new PrintWriter(fileWriterResult);
		Integer falsoPositivo = 0;
		Integer falsoNegativo = 0;
		Integer verdadeiroPositivo = 0;
		Integer verdadeiroNegativo = 0;
		for (int j = 0; j < listaResultadoBio.size(); j++) {
			for (int t = 0; t < listaDataSet.get(j).length(); t++) {
				char caracterLista1 = listaDataSet.get(j).charAt(t);
				char caracterLista2 = listaResultadoMeme.get(j).charAt(t);
				if (Character.isLowerCase(caracterLista1) && Character.isLowerCase(caracterLista2)) {
					falsoPositivo += 1;
				}
				
				if (Character.isUpperCase(caracterLista1) && Character.isUpperCase(caracterLista2)) {
					verdadeiroPositivo += 1;
				}
				
				if (Character.isLowerCase(caracterLista1) && Character.isUpperCase(caracterLista2)) {
					falsoNegativo += 1;
				}
				
				if (Character.isUpperCase(caracterLista1) && Character.isLowerCase(caracterLista2)) {
					verdadeiroNegativo += 1;
				}
			}
		}
		
		String resultado = "resultadoAnalise"
				+ nomeDataSet.substring(0, nomeDataSet.lastIndexOf('.')) + "_v" + (i + 1);
		gerarResultadoAnalise("bio", falsoPositivo, falsoNegativo, verdadeiroPositivo, verdadeiroNegativo, resultado);
		gerarResultadoAnalise("real", 0, 0, 0, 0, null);

		printWriterResult.flush();
		printWriterResult.close();
	}

	private void criarDataSetBioProspector() {
		// CRIAÇÃO DO DATASET QUE SEJA LEGÍVEL PELO BIOPROSPECTOR
		File arq = new File("/home/caio_henrique/DataSets/DataSetBio_" + nomeDataSet);
		try {
			if (!arq.exists()) {
				arq.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(arq, false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for (String linha : listaDataSet) {
				printWriter.println(linha);
			}
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {}
	}

	private void ajustarBioProspector(String pathname) {
		obterDataSetBio("/home/caio_henrique/DataSets/DataSetBio_" + nomeDataSet);
		extrairResultadoBioProspector(pathname);

		// ANALISE E AJUSTE DO RESULTADO PARA QUE POSSAM SER FEITAS AS COMPARAÇÕES
		for (int i = 0; i < listaDataSetBio.size(); i += 2) {
			for (int j = 0; j < listaBio.size(); j += 2) {
				Integer posicaoInicial = listaBio.get(j).lastIndexOf(':');
				Integer posicaoFinal = listaBio.get(j).lastIndexOf('(');
				if (listaDataSetBio.get(i).contains(listaBio.get(j).substring(posicaoInicial + 1, posicaoFinal))
						&& listaBio.get(i).contains("#1")) {
					listaResultadoBio.add(listaDataSetBio.get(i + 1));
					String pos = listaBio.get(j)
							.substring(listaBio.get(j).lastIndexOf('f') + 1, listaBio.get(j).lastIndexOf('.')).trim();
					listaPosicaoComecoMotifResultanteBio.add(Integer.parseInt(pos) - 1);
					break;
				}
			}
		}

		// SEPARANDO CAPA PARTE QUE COMPÕE A SEQUÊNCIA DO DNA DO MOTIF RESULTANTE
		for (int i = 0; i < listaResultadoBio.size(); i++) {
			String primeiraParte = listaResultadoBio.get(i).substring(0, listaPosicaoComecoMotifResultanteBio.get(i));
			String motifResultante = listaResultadoBio.get(i).substring(listaPosicaoComecoMotifResultanteBio.get(i),
					listaPosicaoComecoMotifResultanteBio.get(i) + motifTamanho);
			String segundaParte = listaResultadoBio.get(i).substring(
					listaPosicaoComecoMotifResultanteBio.get(i) + motifTamanho, listaResultadoBio.get(i).length());
			listaResultadoBio.set(i,
					primeiraParte.toLowerCase() + motifResultante.toUpperCase() + segundaParte.toLowerCase());
		}
		
		// TRANSFORMANDO DATASET DE LETRAS PARA NÚMEROS PARA ANÁLISE EM R POSTERIORMENTE
		listaStringResultanteBio = new ArrayList<>();
		for (int i = 0; i < listaResultadoBio.size(); i++) {
			for (int j = 0; j < listaResultadoBio.get(i).length(); j++) {
				if (Character.isUpperCase(listaResultadoBio.get(i).charAt(j))) {
					listaStringResultanteBio.add("1");
				} else {
					listaStringResultanteBio.add("0");
				}
			}
		}
	}

	private void obterDataSetBio(String pathname) {
		String linha = new String();
		File arquivo = new File(pathname);
		if (arquivo.exists()) {
			try {
				FileReader leitorDeArquivo = new FileReader(pathname);
				BufferedReader bufferedReader = new BufferedReader(leitorDeArquivo);
				while (true) {
					linha = bufferedReader.readLine();
					if (Objects.isNull(linha)) {
						break;
					}

					listaDataSetBio.add(linha);
				}
			} catch (Exception exception) {}
		}
	}

	private void extrairResultadoBioProspector(String pathname) {
		// MÉTODO PARA EXTRAÇÃO DA PARTE QUE INTERESSA NO RESULTADO
		int cont = 0;
		String linha = new String();
		File arquivo = new File(pathname);
		if (arquivo.exists()) {
			try {
				FileReader leitorDeArquivo = new FileReader(arquivo);
				BufferedReader bufferedReader = new BufferedReader(leitorDeArquivo);
				while (true) {
					linha = bufferedReader.readLine();
					if (Objects.isNull(linha)) {
						break;
					}

					String validacaoInicio = ">tair";
					String validaFinal = "******************************";
					while (cont == 0) {
						linha = bufferedReader.readLine();
						if (linha.startsWith(validacaoInicio)) {
							cont++;
						}
					}
					
					if (linha.startsWith(validaFinal)) {
						break;
					}

					listaBio.add(linha + ".");
				}
			} catch (Exception exception) {}
		}
	}

	private void gerarResultadoAnalise(String alg, Integer falsoPositivo, Integer falsoNegativo,
			Integer verdadeiroPositivo, Integer verdadeiroNegativo, String resultado) throws IOException {
		// AQUI É GERADO O TXT FINAL CONTENDO O RESULTADO DE CADA PASSAGEM DA ANÁLISE
		Double tpx2 = (double) 2 * verdadeiroPositivo;
		Double pcs = (double) verdadeiroPositivo / (verdadeiroPositivo + falsoPositivo);
		Double rcl = (double) verdadeiroPositivo / (verdadeiroPositivo + falsoNegativo);
		Double fsc = (double) tpx2 / (tpx2 + falsoPositivo + falsoNegativo);
		Double melhorFsc = 0.0;
		if (alg.equals("meme")) {
			printWriterResult.print("	FP");
			printWriterResult.print("	FN");
			printWriterResult.print("	VP");
			printWriterResult.print("	VN");
			printWriterResult.print("	PCS");
			printWriterResult.print("	RCL");
			printWriterResult.println("	FSC");

			printWriterResult.print("Meme ->");
			printWriterResult.print("	" + falsoPositivo);
			printWriterResult.print("	" + falsoNegativo);
			printWriterResult.print("	" + verdadeiroPositivo);
			printWriterResult.print("	" + verdadeiroNegativo);
			printWriterResult.printf("	%.4f", pcs);
			printWriterResult.printf("	%.4f", rcl);
			printWriterResult.printf("	%.4f\n", fsc);
		} else if (alg.equals("bio")){
			printWriterResult.print("BioP ->");
			printWriterResult.print("	" + falsoPositivo);
			printWriterResult.print("	" + falsoNegativo);
			printWriterResult.print("	" + verdadeiroPositivo);
			printWriterResult.print("	" + verdadeiroNegativo);
			printWriterResult.printf("	%.4f", pcs);
			printWriterResult.printf("	%.4f", rcl);
			printWriterResult.printf("	%.4f\n", fsc);
			if (fsc > melhorFsc) {
				melhorFsc = fsc;
				melhorResultado = resultado;
			}
		} else {
			printWriterResult.println("");
			printWriterResult.println("String Resultante Real");
			String strReal = "c(";
			for (Integer i = 0; i < listaStringResultanteReal.size(); i++) {
				if (i == listaStringResultanteReal.size() - 1) {
					strReal += listaStringResultanteReal.get(i) + ")";
				} else {
					strReal += listaStringResultanteReal.get(i) + ", ";
				}
			}
			
			printWriterResult.println(strReal);
			printWriterResult.println("");
			printWriterResult.println("String Resultante Meme");
			String strMeme = "c(";
			for (Integer i = 0; i < listaStringResultanteMeme.size(); i++) {
				if (i == listaStringResultanteMeme.size() - 1) {
					strMeme += listaStringResultanteMeme.get(i) + ")";
				} else {
					strMeme += listaStringResultanteMeme.get(i) + ", ";
				}
			}
			
			printWriterResult.println(strMeme);
			printWriterResult.println("");
			printWriterResult.println("String Resultante Bio");
			String strBio = "c(";
			for (Integer i = 0; i < listaStringResultanteBio.size(); i++) {
				if (i == listaStringResultanteBio.size() - 1) {
					strBio += listaStringResultanteBio.get(i) + ")";
				} else {
					strBio += listaStringResultanteBio.get(i) + ", ";
				}
			}
			
			printWriterResult.println(strBio);
		}
	}

}