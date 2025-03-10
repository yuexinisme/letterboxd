package com.example.demo.controller;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component("player")
@Scope("prototype")
public class Player {

    private Integer id;

    private String name;

    private Integer ranking;

    private String country;

    private Integer age;

    private Integer year;

    public static void main(String[] args) {
        String input = " /charles_19/, /katyahk/, /lightskinwhite/, /luxaspg_13/, /cassiusbaruc/, /jehong/, /nunmoder/, /starlightknight/, /deanamythe/, /doug666/, /mlagosjr/, /oatmilklatte/, /eric09/, /tuscanes/, /ourowncreation/, /flamingos4ever/, /nasaofficial/, /aerountree/, /erikawynn/, /cxtnd/, /kaykutcher/, /theohaegele/, /legsan/, /hamzakhan05/, /ortolans/, /jetlonadow/, /asport/, /jack_darnell/, /paulapp/, /hannah_van/, /chomper94/, /killwertz/, /rylee_mp4/, /bethvance/, /buzzy_/, /silkielord/, /matthewalmont/, /johnchainsaw/, /thewerepyreking/, /geesuss/, /h5ei/, /dustinburton/, /jcmurie/, /smtmrndmdnght/, /joshlikemovie/, /calliepetch/, /rbasmayor/, /redred00/, /lordspongecake/, /nomisyndulla/, /apb_710/, /oldhollyvood/, /ver_joseff/, /jaebursts/, /hilyelo/, /deckk/, /yvespochara/, /bryanonion/, /zyrohrvy/, /bonbonnie/, /hustlebones/, /jordyn28/, /kaydenmovies/, /majorblackjack/, /eyeless_/, /zachthura/, /veganbandolero/, /mvadset/, /joonatanitkonen/, /lukasfllms/, /natc2/, /sebastienbracco/, /jessiedhaliwall/, /myfriendthomas/, /al_garcia/, /cbeckwith/, /mithras_/, /whosebastian/, /robmena24/, /liamconnolly/, /time_ghoul/, /callum2228/, /sulliebud/, /georgevelis/, /eamonnw/, /grweston/, /nbandic/, /trentwayne/, /tomasthechoom/, /kryloren/, /fredm/, /tessajuliette/, /theoverlooked/, /tinuvielsfate/, /thebanjoman/, /wjlowe/, /acccio/, /demdike/, /astrophro/, /delisioso15/, /henry_worm/, /m0thbyte/, /pogiejoe/, /cole444train/, /modish/, /masonpatten/, /sbarlow3/, /goran_mont/, /lilfilm/, /star7leticia/, /bertthebard/, /digitalpress/, /ursainferior/, /kdot1929/, /sidneyuh/, /mrstebo/, /manojap17/, /apollo/, /antcarpendale/, /drakezappa/, /pfadfaog/, /foreverest/, /isaacferguson12/, /b_cool/, /fartsouls/, /itssofial/, /bugsmeany/, /head1236/, /xhille/, /jameservations/, /hilversum/, /bjorkbowie/, /fanoniancatgirl/, /brighght/, /tgetsey/, /alexnichols/, /wheeleyecanbury/, /andy202/, /sirina/, /alyaay/, /benjaminbaron99/, /alisunnysafwan/, /italianspidrman/, /sullyvan/, /bennyinboston/, /sashah/, /blrobin2/, /9voltdc/, /kiauna/, /alisahba/, /littleglimpse/, /travisspazz/, /mistermustang/, /val_the_filmguy/, /mattdigsfilm/, /rainey_dawn/, /jerseydevi1/, /micahfsantos/, /lucyelle/, /simbod/, /transmissionror/, /getaroomyouheck/, /sassynora/, /mitchellshake/, /angelworm/, /cavityinurtooth/, /broch/, /nacholash/, /lilianan/, /demarderozan/, /aurorasfilmsz/, /richarddavis29/, /quentinfourre/, /max__p/, /tamarath/, /hiddly/, /friendshaped/, /nine_e/, /exdeputysonso/, /justinjm/, /caroj_02/, /rockingphilly/, /rexxeno/, /2old2dieyoung/, /insublimation/, /stormreach/, /geoff/, /fynokkya/, /moviesilike08/, /twinpeakswhore/, /thomaseremia/, /arthur_ant18/, /sammytmartin/, /hennymoon/, /mangosavant/, /logiyasser/, /aaliyahgdng/, /maxlpalmer/, /fortuneandgreen/, /greyglassed/, /devanw/, /vielleylieles/, /adamofnebb/, /mikkovsviinikka/, /leviathan89/, /yougomollycoco/, /aphroditemine/, /slowburning/, /jayziegs/, /mymindgrapes/, /thecenobite03/, /lunarmaria/, /wecaneatcereal/, /frodoapologist/, /jakob_mathews/, /michi_kohl/, /michael_o_ryan/, /dpayne/, /mickes/, /foxygrandpa/, /itsdaniiii____/, /mrboffly/, /govindawijaya/, /m34gan/, /lemonlimebeats/, /sam_549/, /nobody42/, /pollyjean37/, /washusha/, /dakariholder/, /ujustdontgetit/, /jaillud/, /ethanhuynhvu/, /bungostraits/, /casketface/, /newbyjosh/, /lucymcclellan/, /j_frechzinaer/, /c_aaron_c/, /stauber/, /byericwebb/, /lawlaah/, /mikefaistluvr/, /elizabethbwmn/, /lumierewaste/, /hotdonkeybear/, /goodatama/, /boston50001/, /pizzahut/, /ieatmovies2/, /paigeirwin/, /deadth_moments/, /chris_niver/, /stevenandrais/, /virtualliv/, /dawnyydarko/, /baroque25/, /vocloz/, /mjmst/, /ghostie13/, /aaamaaa/, /ya_boi_joe/, /rileydreamwood/, /moviemattk/, /endhetoreos/, /ldssg/, /thedeadburger/, /_jackathy_/, /sirdavidcoyne/, /starsmash/, /josefinephway/, /asarker/, /moonshapedpools/, /dinosaurhotline/, /coleprotocol/, /monty11188/, /maddiesiev/, /ghouliayelps/, /robin_virago/, /maxjameshill/, /moviediarycc/, /burnbook/, /nc02/, /tophtough9/, /kataking8/, /nickthemonty/, /m3lanch0l1a/, /whereswingnut/, /shelots/, /callifrax/, /the_moonlighter/, /uno_muroono/, /ghoby/, /charlienorm/, /mcceuan/, /kanjichris/, /coffeenebula/, /spectrumtacular/, /marcopolo_/, /shiku/, /jmack24/, /beignetmanque/, /conmgrigoras/, /tasteslikeevil/, /chelouttahell/, /mkola/, /blackswan66/, /savannahsprice/, /stanleyboobrik/, /hilli_/, /samuel2001/, /ghoby/, /charlienorm/, /mcceuan/, /kanjichris/, /coffeenebula/, /spectrumtacular/, /marcopolo_/, /shiku/, /jmack24/, /beignetmanque/, /conmgrigoras/, /tasteslikeevil/, /chelouttahell/, /mkola/, /blackswan66/, /savannahsprice/, /stanleyboobrik/, /hilli_/, /samuel2001/]";
        String[] names = input.split(",");
        for (String name:names) {
            name = name.trim();
            name = "https://letterboxd.com" + name;
            System.out.println(name);
            System.out.println("\n");
        }
    }
}
