package imagemetadatareader;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ImageMetadataReader {
    public static void main(String[] args) {
        ImageMetadataReader meta = new ImageMetadataReader();
        meta.ler_escrever_xml("download.jpg");
    }

    void ler_escrever_xml( String nomeDoArquivo ) {
        try {
            File file = new File( nomeDoArquivo );
            
            ImageInputStream imagem = ImageIO.createImageInputStream(file);
            
            //retorna um iterável com todos os "ImageReaders" presententes no objeto
            Iterator<ImageReader> iteravel = ImageIO.getImageReaders(imagem);
            
            if (iteravel.hasNext()) {
                ImageReader leitor = iteravel.next();
                
                leitor.setInput(imagem, true);
                IIOMetadata metadata = leitor.getImageMetadata(0);
                String[] nomes = metadata.getMetadataFormatNames();
                int length = nomes.length;
                for (int i = 0; i < length; i++) {
                    System.out.println( "Nome do formato: " + nomes[ i ]);
                    displayMetadata(metadata.getAsTree(nomes[i]));
                }
            }
        }
        catch (Exception ex) {
            System.out.print("Deu Erro: " + ex);
        }
    }

    void displayMetadata(Node raiz) {
        displayMetadata(raiz, 0);
    }

    void indentar(int nivel) {
        for (int i = 0; i < nivel; i++)
            System.out.print("    ");
    }

    void displayMetadata(Node node, int nivel) {
        indentar(nivel);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap atributos = node.getAttributes();
        if (atributos != null) {
            int tamanho = atributos.getLength();
            for (int i = 0; i < tamanho; i++) {
                Node attr = atributos.item(i);
                System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
            }
        }

        Node filho = node.getFirstChild();
        if (filho == null) {
            System.out.println("/>");
            return;
        }
        System.out.println(">");
        while (filho != null) {
            // imprime os filhos (recursivamente)
            displayMetadata(filho, nivel + 1);
            filho = filho.getNextSibling();
        }
        indentar(nivel);
        System.out.println("</" + node.getNodeName() + ">");
    }
}
