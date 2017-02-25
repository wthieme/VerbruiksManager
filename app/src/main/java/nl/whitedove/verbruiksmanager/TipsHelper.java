package nl.whitedove.verbruiksmanager;

import java.util.ArrayList;
import java.util.Random;

class TipsHelper {

    private static final ArrayList<Tip> mLijst = new ArrayList<>();
    private static final Random rnd = new Random();

    private static void nieuweTip(int id, String tekst) {
        Tip tip = new Tip(id, tekst);
        mLijst.add(tip);
    }

    static String GeefTip() {
        MaakLijst();

        int index = rnd.nextInt(mLijst.size());
        return mLijst.get(index).getTekst();
    }

    private static void MaakLijst() {

        if (mLijst.size() != 0) {
            return;
        }

        nieuweTip(1, "De meeste tips zijn gevonden op www.milieucentraal.nl/energie-besparen/. Kijk daar voor nog veel meer tips voor het besparen van energie.");
        nieuweTip(2, "De meeste moderne internet routers zijn softwarematig ’s nachts uit te zetten. Ga daarvoor naar de instellingen van de router.");
        nieuweTip(3, "Ga je een week of langer op vakantie? Schakel alle apparaten uit. Denk daarbij aan modems, routers, TVs , boilers en computers.");
        nieuweTip(4, "De Ziggo Horizon Mediabox gebruikt standaard 38 Watt per uur, ook als er geen TV wordt gekeken. In de “Stand-by-Midden” modus verbruikt hij 27 Watt per uur. Enige nadeel is de opstarttijd van ongeveer 1 minuut als je TV wilt gaan kijken.");
        nieuweTip(5, "Voor je portemonnee en het milieu is het beter om je 10-jaar oude koelkast te vervangen door een A+++ model. Deze zijn namelijk veel energie-zuiniger.");
        nieuweTip(6, "Gebruik een energiemeter om op zoek te gaan naar jouw grootste energieslurper. Goede kandidaten zijn de wasdroger, de CV ketel en een kleine keukenboiler. Maar ook de Ziggo Horizon Mediabox zou wel eens jouw grootste energieslurper kunnen zijn.");
        nieuweTip(7, "Sommige halogeenlampen hebben een transformator die ook stroom gebruikt als de lamp uit is. Zet dan een schakelaar vóór de transformator.");
        nieuweTip(8, "Twijfel je tussen de aanschaf van een desktop, laptop of tablet? Een tablet is het meest energiezuinig.");
        nieuweTip(9, "Bedenk voor aanschaf van een HD-tv of het echt wat oplevert. HD-televisies gebruiken meer stroom, terwijl de hogere beeldresolutie niet altijd zichtbaar is. En decoders die HD-signalen kunnen verwerken gebruiken continu meer stroom dan decoders die dat niet kunnen, zelfs in stand-by.");
        nieuweTip(10, "Stand-by verbruik tegengaan bespaart jaarlijks tientallen euro’s aan stroom. Denk daarbij aan computers, TVs, routers, modems en HiFi-apparatuur.");
        nieuweTip(11, "Stroom uit het stopcontact is zo'n duizend keer goedkoper en veel milieuvriendelijker dan wegwerpbatterijen. Apparaat zonder stekker? Dan zijn oplaadbare batterijen voor het milieu en jouw portemonnee de beste optie.");
        nieuweTip(12, "Doe lampen uit als je een ruimte verlaat (ook als dat kort is). Laat ook energiezuinige lampen niet onnodig aan.");
        nieuweTip(13, "Let altijd op het energielabel voordat je een nieuw apparaat aanschaft.");
        nieuweTip(14, "Heb je een kleine boiler in de keuken. Dat zijn echte energieslurpers. Kijk of je met een kleinere boiler toekan. Hoe kleiner de boiler hoe energiezuiniger hij is.");
        nieuweTip(15, "Vervang gloeilampen door spaarlampen of nog beter door LED lampen. Op www.milieucentraal.nl/energie-besparen/apparaten-en-verlichting/energiezuinig-verlichten/energiezuinige-lamp-kiezen/ staan aanschaf tips.");
        nieuweTip(16, "Bij koelkasten, vriezers, wasmachines en vaatwassers is het A+ label tegenwoordig het slechtste label op de markt. Kies voor A+++; dat is veel zuiniger.");
        nieuweTip(17, "Van al het warme water dat je gebruikt gaat 80 procent naar de douche. Met een waterbesparende douchekop en korter douchen kun je flink besparen op je energieverbruik.");
        nieuweTip(18, "Een huis dat heel goed is geïsoleerd heeft in de winter maar weinig warmte nodig om behaaglijk te zijn. Denk daarbij aan spouwmuurisolatie, vloerisolatie, dakisolatie en HR++ glas.");
        nieuweTip(19, "Een HR-ketel samen met een slimme thermostaat is een goede keuze om zuinig met gas om te gaan.");
        nieuweTip(20, "Infraroodpanelen geven stralingswarmte, net als gloeiende houtblokken. Ze zijn een zuinige bijverwarming als je kort in een kamer bent.");
        nieuweTip(21, "Denk je aan het kopen van zonnepanelen? Je kan op de installatiekosten besparen als je ze samen met je buren aanschaft. Hoe meer mensen meedoen hoe goedkoper het wordt.");
        nieuweTip(22, "Zonnepanelen op het westen of oosten en een dakhelling van 40 graden halen een opbrengst van 80% ten opzichte van de maximumopbrengst op het zuiden. Dus ook dan kan de aanschaf van panelen nog lonen.");
        nieuweTip(23, "Met een energieverbruiksmanager krijg je inzicht in je energieverbruik. Kijk op www.energieverbruiksmanagers.nl");
        nieuweTip(24, "Koop een douche zandloper of douche coach. Daarmee hou je de douchetijd in de hand en bespaar je energie.");
        nieuweTip(25, "Vergelijk je energie jaarrekening met die van je buren. Zijn er grote verschillen? Ga dan na waar jij of je buren nog winst kan boeken.");
        nieuweTip(26, "Ontdooi je vriezer regelmatig. Daarmee kan je tot wel 70 kWh besparen.");
        nieuweTip(27, "Gebruik niet te grote pannen bij het koken als dat niet nodig is.");
        nieuweTip(28, "Plaats tochtstrippen op deur en raam kozijnen.");
        nieuweTip(29, "Doe tijdens het eten koken de deksels op de pannen zo verlies je minder warmte en dat zorgt weer voor energiebesparing.");
        nieuweTip(30, "Regenwater opvangen om daar de tuin mee te sproeien.");
        nieuweTip(31, "Plaatst een tochtborstel achter de brievenbus.");
        nieuweTip(32, "Zet de computer in de slaapstand als je hem even niet gebruikt. Met een SSD harde schijf start hij razendsnel weer op.");
        nieuweTip(33, "Breng je autobanden op spanning. Een te lage bandenspanning kost extra energie en is bovendien gevaarlijk.");
        nieuweTip(34, "Radiatorfolie is een snelle en goedkope manier om geld en energie te besparen. Het voorkomt dat je veel warmte verliest via je muur of raam.");
        nieuweTip(35, "Laat de was buiten drogen in plaats van in de wasdroger.");
        nieuweTip(36, "Zet de thermostaat een graadje lager en doe daarvoor in de plaats een warme trui aan");
        nieuweTip(37, "Vergeet niet de thermostaat een uur voordat je naar bed gaat alvast laag te zetten. De kamer blijft zolang nog warm, maar je stookt niks.");
        nieuweTip(38, "Was op lagere temperaturen. Vrijwel alle wasmiddelen wassen goed schoon op 30 graden. Dat is veel zuiniger dan wassen op 40 of 60 graden.");
        nieuweTip(39, "In de stad is een ritje van een paar kilometer is op de fiets even snel als met de auto. Bovendien ook gezond. En altijd veel goedkoper. Geen benzine of diesel en evenmin parkeergeld.");
        nieuweTip(40, "Ook voor de elektrische waterkoker geldt: doe er niet meer water in dan u nodig heeft. Het water is sneller warm en het kost minder energie!");
        nieuweTip(41, "Bevroren eten ontdooit u het beste in de koelkast. Energiezuinig, want dat bevroren voedsel zorgt ervoor dat de koelkast zelf minder hoeft te koelen. Stel de koelkast trouwens op zes graden en de vriezer op minus achttien graden in.");
        nieuweTip(42, "Met een stekkerdoos met een schakelaar zet u in één moeite alles echt uit. Of gebruik een standby-killer. Dan zet u de televisie gewoon met de afstandsbediening aan of helemaal uit.");
        nieuweTip(43, "Houd binnendeuren gesloten, maar blijf altijd ventileren! Ventileren doet u door ventilatieroosters en klepraampjes open te zetten. Daarnaast dient u dagelijks twintig minuten te luchten door een raam open te zetten.");

    }
}
