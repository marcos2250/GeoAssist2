package geoassist.geometria;

import static geoassist.misc.Utils.decToStr;
import static geoassist.misc.Utils.round;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import geoassist.dominio.Datum;
import geoassist.dominio.Imovel;

import java.util.Arrays;

public class ProjecaoUtils {

    public static final double Pi = 3.14159265358979;
    public static final double deg2rad = 1.74532925199433E-02;

    public static Datum criaDatum(String datumCodigo, byte zona, String letra) {

        // TODO instanciar objeto Datum preenchido com valores identificados
        // pelo "datumCodigo"
        Datum datum = new Datum();

        if (datum.nome == "") {
            return null;
        }

        datum.k0 = 0.9996; // Fator de Escala K
        datum.zona = zona;
        datum.letra = letra;

        // Fator de achatamento
        datum.fA = sqrt(pow(datum.er, 2) - pow(datum.erMenor, 2)) / datum.er;
        
        datum.eS = pow(datum.fA, 2);

        // Quadrado da Ecentricidade
        // Datum.eS = (2 * (1 / Datum.fA)) - ((1 / Datum.fA) ^ 2)
        // Datum.fA = strToDec(v5)

        datum.MC = MeridianoCentral(zona); // Meridiano Central

        return datum;
    }

    public static byte MeridianoCentral(int zona) {
        return (byte) ((zona - 1) * 6 - 180 + 3);
    }

    public static int ZonaUTMNumero(double Longitude) {
        if (Longitude < 0) {
            return (int) floor((180 + Longitude) / 6) + 1;
        } else {
            return (int) floor((Longitude / 6) + 31);
        }
    }

    public static String ZonaUTMLetra(double Latitude) {
        return "ABCDEFGHJKLMNPQRSTUVWXYZ".substring((int) floor(Latitude / 8 - 0.5 + 13), 1);
    }

    public static double FatorK(Datum datum, double Latitude, double Longitude) {
        // Calcula o Fator de Escala K
        // FatorK = Ko / sqrt(1 - (cos(Latitude) * sin(Longitude - MC)) ^ 2)

        // SetDatum Datum.Nome, ZonaUTMNumero((Longitude)),
        // ZonaUTMLetra((Latitude))
        return datum.k0 / sqrt(1 - pow((cos(Latitude * deg2rad) * sin((Longitude - datum.MC) * deg2rad)), 2));
    }

    public static double FatorK_UTM(Datum datum, double Easting, double Northing) {
        // Calcula o Fator de Escala baseando em coordenadas UTM
        double alpha, M25, N25, O25, nAux, mAux;

        alpha = 1.00505262472535 * datum.er * (1 - datum.eS) * deg2rad;

        M25 = Northing - (10 ^ 7);
        N25 = Easting - 500000;

        O25 = M25 / (0.9996 * alpha * (180 / (atan(1) * 4)));

        nAux = datum.er / pow((1 - datum.eS * (pow(sin(O25), 2))), (0.5));
        mAux = (datum.er * (1 - datum.eS)) / (pow((1 - datum.eS * (pow(sin(O25), 2))), (3 / 2)));

        return 0.9996 * (1 + (pow(N25, 2) / (2 * nAux * mAux)) + (pow(N25, 4) / (24 * pow(nAux, 2) * Math.pow(mAux, 2))));

        // Fonte:
        // www.topografia.com.br/br/downloads/UTMGEO//GEOUTM.xls
    }

    public static double ConvMeridiana(Datum datum, double Latitude, double Longitude) {
        // Calcula a Convergencia Meridiana (valores em graus decimais)
        // SetDatum Datum.Nome, ZonaUTMNumero((Longitude)),
        // ZonaUTMLetra((Latitude))
        return (((Longitude - datum.MC) * deg2rad) * sin(Latitude * deg2rad)) / deg2rad;
        // Fonte
        // http://www.geodesia.ufrgs.br/trabalhosdidaticos/Topografia//Aplicada//A//Engenharia//Civil/Apostila/Apostila//TopoAplicadaEng//2007.pdf
    }

    public static void LL2UTM(Datum datum, double Easting, double Northing, double Latitude, double Longitude) {
        // Converte coordenadas geograficas Latitude / Longitude para UTM
        double LatRad, LongRad;
        double LongOrigin, LongOriginRad;
        int zonenumber;
        double eccSquared, eccPrimeSquared;
        double n, T, c, a, M, ER, k0;
        double UTMEasting, UTMNorthing;

        ER = datum.er;
        k0 = datum.k0;
        eccSquared = datum.eS;

        LatRad = Latitude * deg2rad; // converte para radianos
        LongRad = Longitude * deg2rad;

        zonenumber = (int) (floor((Longitude + 180) / 6) + 1);

        LongOrigin = (zonenumber - 1) * 6 - 180 + 3; // +3 puts origin in middle
                                                     // of zone
        LongOriginRad = LongOrigin * deg2rad;

        eccPrimeSquared = (eccSquared) / (1 - eccSquared);

        n = ER / sqrt(1 - eccSquared * sin(LatRad) * sin(LatRad));
        T = tan(LatRad) * tan(LatRad);
        c = eccPrimeSquared * cos(LatRad) * cos(LatRad);
        a = cos(LatRad) * (LongRad - LongOriginRad);

        M = ER
                * ((1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5 * eccSquared * eccSquared * eccSquared
                        / 256)
                        * LatRad //
                        - (3 * eccSquared / 8 + 3 * eccSquared * eccSquared / 32 + 45 * eccSquared * eccSquared
                                * eccSquared / 1024)
                        * sin(2 * LatRad) //
                        + (15 * eccSquared * eccSquared / 256 + 45 * eccSquared * eccSquared * eccSquared / 1024)
                        * sin(4 * LatRad) //
                - (35 * eccSquared * eccSquared * eccSquared / 3072) * sin(6 * LatRad));

        UTMEasting = (k0 * n * (a + (1 - T + c) * a * a * a / 6 //
        + (5 - 18 * T + T * T + 72 * c - 58 * eccPrimeSquared) * a * a * a * a * a / 120) //
        + 500000);

        UTMNorthing = (k0 * (M + n * tan(LatRad) * (a * a / 2 + (5 - T + 9 * c + 4 * c * c) * a * a * a * a / 24 //
        + (61 - 58 * T + T * T + 600 * c - 330 * eccPrimeSquared) * a * a * a * a * a * a / 720)));

        if ((Latitude < 0)) {
            // 10000000 meter offset for southern hemisphere
            UTMNorthing = UTMNorthing + 10000000;
        }

        Easting = round(UTMEasting, 2);
        Northing = round(UTMNorthing, 2);

        // Bibliografia
        // http://www.gpsy.com/gpsinfo/geotoutm/
    }

    public static void UTM2LL(Datum datum, double Latitude, double Longitude, double Easting, double Northing) {
        // Converte coordenadas UTM para coordenadas geograficas Latitude /
        // Longitude
        double LatRad, LongRad;
        double LongOrigin;
        int zonenumber;
        double eccSquared, eccPrimeSquared;
        double e1, N1, T1, C1, R1, D, M;
        double ER, k0;
        double mu, phi1Rad;
        double X, Y;

        // SetDatum DatumNome, ZonaNumero, ZonaLetra
        ER = datum.er;
        k0 = datum.k0;
        eccSquared = datum.eS;
        zonenumber = datum.zona;

        if (Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M").contains(datum.letra)) {
            // remove 10,000,000 meter offset used for southern hemisphere
            Y = Northing - 10000000;
        } else {
            Y = Northing;
        }

        // remove 500,000 meter offset for longitude
        X = Easting - 500000;

        // +3 puts origin in middle of zone
        LongOrigin = (zonenumber - 1) * 6 - 180 + 3;

        M = Y / k0;

        e1 = (1 - sqrt(1 - eccSquared)) / (1 + sqrt(1 - eccSquared));

        eccPrimeSquared = (eccSquared) / (1 - eccSquared);
        Y = Northing;

        mu = M / (ER * (1 - ((eccSquared) / 4) - (3 * pow(eccSquared, 2) / 64) - (5 * pow(eccSquared, 3) / 256)));

        phi1Rad = mu + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * sin(2 * mu) //
                + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * sin(4 * mu) //
                + (151 * e1 * e1 * e1 / 96) * sin(6 * mu);

        N1 = ER / sqrt(1 - (eccSquared * sin(phi1Rad) * sin(phi1Rad)));
        T1 = tan(phi1Rad) * tan(phi1Rad);
        C1 = eccPrimeSquared * cos(phi1Rad) * cos(phi1Rad);
        R1 = ER * (1 - eccSquared) / (pow(1 - eccSquared * sin(phi1Rad) * sin(phi1Rad), 1.5));
        D = X / (N1 * k0);

        LatRad = phi1Rad
                - (N1 * tan(phi1Rad) / R1)
                * (D * D / 2 - (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * eccPrimeSquared) * D * D * D * D / 24 //
                + (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * eccPrimeSquared - 3 * C1 * C1) * D * D * D * D * D
                        * D / 720);

        LongRad = (D - (1 + 2 * T1 + C1) * D * D * D / 6 + (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * eccPrimeSquared + 24
                * T1 * T1) //
                * D * D * D * D * D / 120)
                / cos(phi1Rad);

        Latitude = round(LatRad / deg2rad, 8);
        Longitude = round(LongOrigin + LongRad / deg2rad, 8);

        // Bibliografia
        // http://www.gpsy.com/gpsinfo/geotoutm/
    }

    public static void ConverteDatums(Datum datum, double latitude_destino, double longitude_destino, double altura_destino,
            String datumDestino, double latitude_origem, double longitude_origem, double altura_origem,
            String datumorigem) {
        // Converte coordenadas Lat/Long entre SIRGAS e SAD69
        Datum dtOrigem, dtDestino;
        double nOrigem;
        double Longitude, Latitude;
        double X, Y, Z;
        int sgnLat, sgnLon;

        if (latitude_origem > 0) {
            sgnLat = 1;
        } else {
            sgnLat = -1;
        }

        if (longitude_origem > 0) {
            sgnLon = 1;
        } else {
            sgnLon = -1;
        }

        dtOrigem = criaDatum(datumorigem, datum.zona, datum.letra);
        dtDestino = criaDatum(datumDestino, datum.zona, datum.letra);

        nOrigem = dtOrigem.er / (sqrt(1 - dtOrigem.eS * pow(sin(latitude_origem * deg2rad), 2)));

        X = (nOrigem + altura_origem) * cos(latitude_origem * deg2rad) * cos(longitude_origem * deg2rad);
        Y = (nOrigem + altura_origem) * cos(latitude_origem * deg2rad) * sin(longitude_origem * deg2rad);
        Z = (nOrigem * (1 - dtOrigem.eS) + altura_origem) * sin(latitude_origem * deg2rad);

        if (datumorigem == "SAD69" && datumDestino == "SIRGAS") {
            X = X - 67.35; // deltax
            Y = Y + 3.88; // deltay
            Z = Z - 38.22; // deltaz
        }

        if (datumorigem == "SIRGAS" && datumDestino == "SAD69") {
            X = X + 67.35; // deltax
            Y = Y - 3.88; // deltay
            Z = Z + 38.22; // deltaz
        }

        Longitude = abs(atan(Y / X));

        Latitude = abs((atan((Z + (dtDestino.erMenor * ((pow(dtDestino.er, 2) - pow(dtDestino.erMenor, 2)) / pow(
                dtDestino.erMenor, 2)))
                * pow((sin((atan(((Z) / (sqrt(pow(X, 2) + pow(Y, 2)))) * (dtDestino.er / dtDestino.erMenor))))), 3))
                / ((sqrt(pow(X, 2) + pow(Y, 2))) - (dtDestino.er * ((pow(dtDestino.er, 2) - pow(dtDestino.erMenor, 2)) / pow(
                        dtDestino.er, 2)))
                        * pow((cos((atan(((Z) / (sqrt(pow(X, 2) + pow(Y, 2)))) * (dtDestino.er / dtDestino.erMenor))))),
                                3)))));

        altura_destino = ((sqrt(pow(X, 2) + pow(Y, 2))))
                / cos(Latitude)
                - ((dtDestino.er) / pow(
                        (1 - ((pow(dtDestino.er, 2) - pow(dtDestino.erMenor, 2)) / pow(dtDestino.er, 2))
                                * pow(sin((((atan((Z + (dtDestino.erMenor * ((pow(dtDestino.er, 2) - pow(
                                        dtDestino.erMenor, 2)) / pow(dtDestino.erMenor, 2)))
                                        * pow((sin((atan(((Z) / (sqrt(pow(X, 2) + pow(Y, 2))))
                                                * (dtDestino.er / dtDestino.erMenor))))), 3))
                                        / ((sqrt(pow(X, 2) + pow(Y, 2))) - (dtDestino.er * ((pow(dtDestino.er, 2) - pow(
                                                dtDestino.erMenor, 2)) / pow(dtDestino.er, 2)))
                                                * pow((cos((atan(((Z) / (sqrt(pow(X, 2) + pow(Y, 2))))
                                                        * (dtDestino.er / dtDestino.erMenor))))), 3))))))), 2)),
                        (1 / 2)));

        longitude_destino = (Longitude / deg2rad) * sgnLat;
        latitude_destino = (Latitude / deg2rad) * sgnLon;

        // Fonte:
        // www.topografia.com.br/br/downloads/UTMGEO//GEOUTM.xls
    }

    public static double AnguloCoord(double X1, double Y1, double X2, double Y2) {
        // Calcula o angulo entre duas coordenadas num plano cartesiano

        if (X1 == X2 && Y1 == Y2) {
            return 0;
        }

        if (Y2 > Y1) {
            if (X2 > X1) {
                return (Pi / 2) - atan(abs(Y2 - Y1) / abs(X2 - X1));
            } else {
                if (X1 != X2) {
                    return atan(abs(Y2 - Y1) / abs(X1 - X2)) + (3 * (Pi / 2));
                } else {
                    return 0;
                }
            }
        } else {
            if (X2 > X1) {
                return atan(abs(Y1 - Y2) / abs(X2 - X1)) + (Pi / 2);
            } else {
                if (X1 != X2) {
                    return (3 * (Pi / 2)) - atan(abs(Y1 - Y2) / abs(X1 - X2));
                } else {
                    return Pi;
                }
            }
        }

    }

    public static double DistM(double X1, double Y1, double X2, double Y2) {
        // Calcula a distancia entre dois pontos (Pitagoras)
        return sqrt(pow(X1 - X2, 2) + pow(Y1 - Y2, 2));
    }

    public static void CalculaAreaPerim(Imovel pImovel) {
        double pArea, pPerim;
        double p1X, p1Y;
        double p2X, p2Y;

        pPerim = 0;
        pArea = 0;

        for (int i = 1; i < pImovel.getNumeroDePontos(); i++) {

            p1X = pImovel.getCoordenada(i).x;
            p1Y = pImovel.getCoordenada(i).y;

            if (i < pImovel.getNumeroDePontos()) {
                p2X = pImovel.getCoordenada(i + 1).x;
                p2Y = pImovel.getCoordenada(i + 1).y;
            } else {
                p2X = pImovel.getCoordenada(1).x;
                p2Y = pImovel.getCoordenada(1).y;
            }

            // Fórmulas de area e perimetro
            pPerim = pPerim + round(sqrt((pow(abs(p1Y - p2Y), 2)) + (pow(abs(p1X - p2X), 2))), 2);
            pArea = pArea + round(((p1X * p2Y) - (p1Y * p2X)), 4);
        }

        // Correcao da area e retorno dos valores
        String strArea, strPerimetro, strNumCoords;
        strArea = decToStr(abs(pArea / 20000), 4);
        strPerimetro = decToStr(pPerim, 2);
        strNumCoords = String.valueOf(pImovel.getNumeroDePontos());

        // WriteParamData "CAMPO//AREAMED", strArea
        // WriteParamData "CAMPO//PERIMETRO", strPerimetro
        // WriteParamData "CAMPO//NUMPONTOS", strNumCoords

        // form1.Caption = "Área: " + strArea + " ha, Perímetro " + strPerimetro
        // + " m, Vértices: " + strNumCoords;

        System.out.println(strArea + ", " + strPerimetro + ", " + strNumCoords);

    }

}
