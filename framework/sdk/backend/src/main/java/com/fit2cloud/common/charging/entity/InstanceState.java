package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/22  14:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class InstanceState {

    /**
     * 实例状态
     */
    private final List<State> stateList;
    /**
     * 实例进制
     */
    private final int radix = 10;

    private int mark;

    public List<State> getStateList() {
        return stateList;
    }

    public static void main(String[] args) {
        Time of = Time.of(01, 2, 2);
        System.out.println(of.toString());
        InstanceState a = new InstanceState("bdqqcwz8btuehjafy2rv9nopauoggym5ynva9412hdalxcfz5aousbyvrrodh9y2vr7bn4m5b9gb8q7gcpymp9ajvn44jd5uuwo036q0b9914t1xxwsjzpyrxh0r8uy7jytf6tquwjsbz1xuoe3z4oae3x1xxiefjatlz1t2g78u8zwgub9j5845610uhrb6grlqmvoc2dkzgo7y16g3dhjjuglggznb3b55igjnlsxcdxbhjo9aqqak6f7tlhhfbpr1cazk73zek7kp6zk7dzj508lrbn6mdzkksuflo95kq44zi04kxv5ey0okbij1xbbt4iymqzm9skqv1mqm79873rthwyqro5q8l0a0er6mg2lqkjqjtvbq9io0ejk53xjp7r7jayeu18rka7q5n8pwaq3cp1nh4bkrsxt8aip9yw28utb3t5m1a2phrrq005jddjeub9xor278nthe2l62yku5myk2vgfz7n62cdxeqpaqtax2pscbjtg9n0sl7mkb8f8jq3syx2py3z9q4wqd537abfwyuuyfrjikvr7myr8wxrlv9l60vttbc1xfdl8m2vj8wanbmnhrdumnchs1gtqj0qzbpsu4xm3g4r7r9lv6d8hhjlagqlrfzkh5uwbxufolbfk3umjupzkh0hzzk80wa1f6sjrabui782gtcu09t0ntc94o9ixrtpa2mnwqeqd76m143ehb33uy9jfu0g97n5v9xvl26toluqc28b4ztjw71aig5m1rl734f2d0d7akga241kdrnfk0ke392oohqkhgwvdv7hgbcut9u6crjselg6d0v69tpwxyk5q75t28buvixgnmgzi4br369lwpf107oow8jb79bjqjzea4m7bpur421t9lfdidk76oveh0hk9z14i6j78zzcgq5vtepii2nnj0k0omsrckhszbknfkrhkdbifdribp4kekhghb9zhzb6y14s4retseer43ser3yl4lpyka74j8i9han7ab8us5mfsg4xl0gqf99hnpry0gvy0bl4l8ag60eedcdx10mq0iyr1nolohp8fnkl8e8kmt2y0u7hmbb3nw4nds9eiqccbrn32hap2xsxg5v3b3tb1kzzyx58ptlhngbp0hui78eo3cep22r0yaecb2juu46wbr9hsvtn5ilqxqixexu3paamce5y9gw3g6vjm4v0gg32r427wpsjthspaewxg5lkx1dsz2ol6ejhszfzh27798dsygpbb28etifc6m3wztm66a3qwd2uwwgknzbil88tv0boqetwcrmkg0kpz3pl0zi957vmas0bbt5rv415c8lrkkwjscptilflozc3ff37g48eb43fm0ydcxa4ggsa07rgyhbzw08ov1ishc4uiluf1em3ger40t5x560s2rmlkr4u3kwcnqsjm34ppm18x1d8z4nm897ef21erc4wfstkzcci6umlelt2989108t8c2qq2x4391v7drtbo28it2m7cb7xrb5sz2u5efjfx4lzo745l6vi4igjqzmdzfkzpu3oiy8kui6lvk6t0ca3s6tjkp1mldu9gaptumaznu23gharr5og80b9cxiyvtqxa0qanwlh0q0if2br7mpcxvwuz6sbb6tww6tnr2fd013084jp540o1p7b8nz0txp3e7tmx6xgzondxzuwwdacgjrb1zzi8yf5ysccaqiv29zmolb1k0pro8g0tiznvsx1cpqnipylh4tyl2ryl9f1mbdeuy7endy00wbgysvn3qt1tb38bndxjnut7n2i1fzhfh2w5banskx8wgrkmscd2evukvbfya3rumj4c2k5tdz3zwzerjjpqrirb0debsbmcd1fgq3cxyuksgqj8yf9qlmmfyniz2fvesgf2j4mwedhti9nh17v7u1uvcykflqkhegh2otgjn4tcmbwqrt5ggr3pxa73mgiph1i6fzd9866knrhx39xhe52ngdusdr7zw2twa4j4upqhnp5cx6k4xzczltcuosdakkqp3b8tz5d82v4e0bj0rqa17juyedg3fdm0o90fim9bum790qw9fnxl5pc6hckmefln2upuw8e0h7jofcc6e71ucif0qbeqy0ql8dm5z6kd8e5reg1zzxrmsbaq1thzciosob6s0a8frqrssn2v5f8gzallizi74j3p0n6onxh9dp926lnthvxqw58vsptjh5vw0as29n6y8ysl92sup1sjokfnqoe1ai8kzo40z0209v9gn6sen9ast1jb51zbo1ajiljbx43qf3euwt67ma05ro8w7lxwi8n09p4swir8z0hd6e4cb653o0mqpdlmrazlzu6causz55vapk2j9h59atjn22ftesk1hh04lz5qim2cl7pq1fh8b6tc3c1qlwh7mfx0yvkfiepqxe46mzsh8e5k8n9sx30c77sb5wq0n0ei5gmvongtu64ivv6gndaj1dr6365sav29slu8t9s6tyy47hhrblj5i36lwh48rnvx3o3cuu1dpu8cz5juo3nn6jonjsxymf73cxcv5r9p09zjlfcfe8tykz98hv8xbo7uwa00wjv8vw2puduz4kxuoincludm5y61dnsrvd2chbhbnfvsyqlafbxvl138b2kz3recfj6s4x1653tcvzde1qwuyi0vwljfypbq5ri9ijvuumchoe5zl5y4jol514ltgxzpbbck85v6ib8wlckm6w0i0m8vg4rfsxycvbegyv5hr64ec7gjb391tuhlxmi9djphnseleyph0l5qa8qh1s2k5wswsmmf8i7oksxgdsppkhqrfhhe03aqyjnmbf3fs51yckb7po9algk21s0wn2n83c6257u5lojko43qsxon8q91fgv4xjnt2qqjmiyrqi7biggwbio9udy0z8n7jrngi60oygfcuty97ig4zy3gydc4e9bhdjkteyj0sslydlm7rjia8zyk27ri1sua4kes6cpwitngaw5wh8207vdjx6707c4udxesvsthxedtsz803c2kyjw9zy15m73874jm94k7kmimv2zl06lbmyk9snke1wu9po1xvokns6dh170h4n7o88g5kg6vxjbr84zu02ixssgehzbqochx7sz5vjb5j0h2kl42e4duoczyoketes8fgic5uhjnmati3xahpf29vc4gilh6e2hr6kliwxlndfcg4lgbew49bfedd3yf8z5r9offjqw8puon7axz18n1xazaas67ihbuyn2s9th2ciki6c68s9r58mnaue0adhepok3m6p2tfoj7iex8olvmh0qxqcuihks8cvni0t80im0fruyy5p09han7xoiwoc55m4besgizk8hl9bj9czshbnnmbotrcoqx1uoaamjhew6l0r4keqpogqlw5g3yb7yh6igjo4g0pxinmciyq05jbee3je312uo8no29fcqexrtlrl3yviirb0gdv10m5cz9pwzh67si26d5a82lvb7cpu1e4lraazo44ia3shkranag2cuts401aromxspxtx38a3ji2qigr3qw7vax88g5ycexmsxi7at5oery8l2829qnu3a201sa729hgoeh1qqobxkyd10yvi5dqpkutoutguonmg5sz70fwg3rxt6yiqg9hc6eoa2s5r1e1h88owarf49r7yfhrc0p06srjw8p4wv1oj49s6h3rts54h0rqcgjm9xsjzn676ly04pn89mtg7m0cg5peaimygywnw55n1npjfvttyc1a317r5do2a014ijyuwgo6ndodkx2vzrn5p8gmv22ud2zy8zikjwq84xektyig1myfof97h14iw081a57nv7sxt0c1kk0hgnsubujwdsnz2bulk8g62y6mkq7pevrr0xgxycaw2y2pt7ksnlenxzxiravwgjegvthwuwru03fvwey8hn1rg53s4b1xv2lhomzgtw8srnq98vjsld6lsmvp04rreh5wbgvdfo9tem712jkthp5dqdwbddydjb57j9p3jdzkw7533jry2y72uqlz4ys3jegf9nt5esciehkm3ppw90y6eskmubm4ad5qf7ueo60ffdd5c1mtj45wfac2v642yqg511x5jwriqq2t0rhk0087e9yr7rcu95vc2c4ki3fbsr4w4dxo2xxwdi2kqxn77k8xqssi01a08f2ql27n1mdhv8klgtupm97hhqq6dkyq2ghjl99sb6tzjvtr47gcevlhdotr1amxgnag3yhqm2pic4vqh45kzgfciodq1x7y25i7g28x20tp8199lj1ft33rarfoy17mkik3xi7ntytqfe6zavifkbmuad0dn1addqekp63d278ajmdxuahk3dkx8shomeq9nwl6p212w6kfmgecer8k8xpru2189rxg7xlhl1je9ceg0sm7us2dj3lqj0e5fmjs32xpci5gt8y98rjypd469zmd46pzij9xp3veflrvnaixshqylfudhirmi48c58ezrqmpyzzwiyhlct706uo8pcg6p3f7en3u9wekgjxyza0h95tkph1gaqaoiti14wdcmofgrfxfdo2h4w08w7i10banncnaozrkidvu2vz1bshjhnxiq5b1xmxmeoj9ohxyfthn5hpqpq0dtjki1jbj1y9ssv5ewregym7touablrrq27b0gbpdf6yge92cywyjl5bhiwsgeuihj6gxt3tc082njnkvy4fobrdy9qajhwwc89d7i6ckb8ktbz3aypmjtzdan41cbkvi6nz1qbf2qmzcu8g81bkg59jn6yy8nq2r0xm370cd0kmxvbhwfynd10zfa2lcr8d4ya6lgusx2hrndntd65vcmq4ge4ph668ni7a9ai8s3sh5r91qq0ehr1iukjf9tx1w8q22se9invgmh18365mpocpsu8qo1mhaks7i2blazijec9dk2wj4ajc8ns533jjo2rzaq7yydk89qm2r77agn00b1h9xl7hwe6m9mxomu16gfck4tx761bz1q3bx5t8ibd16u33gjehoitlkh335jn2h7r4jvkfackt8wplq4kh4xrmopfygy8jitu5i4q1aoib81789ya2lhobw1mmm6psr337w01nzo2ui5985zl430pi2v4p1u9qq3eq3f0z4smx7b4eldu42ci621i51oxh3wd0v6vih8hjny9etskt2povanxnbozny24quwhv97yihf5ozz4gp7ah5n48wu4cx82ytc2hhsdfw8qevri84pka7pq6kmj5q54yrtcwvhf1svn8dw4aefjsjmdm9ak3t1f46gsduzqxqlml7wccoqqot2bk3vomjpqu402usag5vjesncp55dbizu7f1drlmi5yv4uujl3jyuelip19i0boee716ilw308hunxsfoukuq6d3xipoly01f2179t6j3tzhxom04rs48n4aqucm8g1u07u44i7p0animtnbgdwnizruxsykfze6j3t5cpakl52t7hszqjlxlymq04h4lpzw73lwir32bxsl2m12hxlwzgmkka5t94yor22ypeqqjkm7d5qmu9josf3tvvj797bo40j0ojcvuroxfrg4x4nquaeydxlknarxntirjubhpqvayyovstkdyfw3py0dxbmppnrh20jqj88g4g2fkxx06puungkvhvjglhhkrhfiy8x5dyxdstk3z5e70s5o8jdnwcg4wn2cvmt4yfp0r974uapckwzb0bf3298t48uzkke00sd7za25ed6iz60azykxi5cfj4tbzydi7aal5yes5m6tx2nsahwayjzvcm189vcn1ybch8b8yhnxwgkje61lgpftmofbcpf3239wflaqqllw1kyphxc9e5kscggd2uz8bmv1bvfzpxdblgxamwdc8waba3lg1gx6k6auj6rswicmr2yvwi3vfonehsyvbf2kpei1wp2bgfu7uwg3pat5l3e4an50a8ya3t5ndegbdmg46wvhojxs6mar0d6peh9486hzkp5dhuylwpa680vmfe9zbmm9hqx3d63c2dsclcd28tsvumrn66hfsay5hfpnmyq3i5ujttn3jc1lh60pz4wrp5yh4i2vp9z4orr3aw2p5q742y6bhv3b961fcgw6kd6remqhuj9cickwhfvwi6gszfwsdz83h0ey9fv9k578fkwa96m43b4rqvcgxgobgwh3grmfo9vjgtdpk1dlyhh538ggn7auemofh2jjtmtvffk9ie14oo0mquqy53ox7jtc34mfter44fyz0z4pf2zkl9bn8kdkgug8lqqan7x16kwngk3kp29fvdjtpctev32m72glffq9gsv832w9mwo3xo0cw2zu669xcodbx1pqfztxsn1tjwm48su5mw8zfgo6jk3hjqk1qi58kawn5chcmznvyj75d7sjv5dvpp4nzqqo7944jn5m1rj4vafktv2cluvoa9yhe9uzfw5nvjbilimh5hs745gs25cb1ap7z3cmsiysx3gcfrkc1fssr1r88c98z7xin78wv1turpg9ol4o951yqejbp051ydke8o4od0400ibztxf9co36o5lv0jn3hfnhj77nlna5vclkoiyls0m0g7tlem3ixo9kf0q1s8bzcuogngljms124br4afwbd21ej49vnau5ssckfc4syndelq4nygxu7shiuevzl6an5yx4q3fjgldmj41h6mwj7034mk0rc2vzoufvw175nuvtom2j82r2mcgi32fif1yvv3dgdfbwuieorf67iep756ck71f82u8c3kj70opmo0ggqoobry1bendrkbkisfrsvtf4ixdpptf63980bsvzwauucv1ppb554xth5nl0l17ps55wmspo5ikbdwazsg1yxfww0af860smv0tdb6t5aauc2i71xg98p04jl3n4w42u5kcxn531i7xzy9gazzunmz328zt4v892cg7otv6i3hfdy125kt39v1ji4bwupkzdyk52fwd02j93des0wkci2l2y2r6cgd49r4f4z0f93f6arbehspxjxhhzpemwnc0srwe14g5i73vop2woba09vqeyc3vaetvt5fd4xnpamzorbht60kjc10dq4yld7jcu3be5em5wceutut17wtrv1cm7fjib7f1q0lnav29norexu48jbyu3klv6zckc67pzyc7qjevz7ayvtgo6uxmoxj9lmqlr0auqde6ksb0z0wp2addirsmedlppbdmg61xhio8opwrv3gufjsutwc2rd7r162qz8aen5o3hb07a9kmxpeh9b9vgbkd909hfomv0l0r8lsx9lh68jwi06votzwpdew50cn4hgh9nimuexrqr3dgdqf5ap3dfxi6i6uimpm2jpsyqaddc5bs6mk2e0fpo5xklvn03swtht66ssx08vk5vk8b58mf44ga6wp0h0rz04qjlttndddqwvmvrbhcm4jkq660gwbk4zy5040qcb2uaioinb4v84wjap80qexu24y290j7fllvnkdgyl5x1vqyvgyf0ivt34vfdfl5eigsnyc8qex8k9jolfoso3j81xe0j0r8w5jumjnmkr4atu8b6g7zmya17psolexc3ki1u9irs1sxgi0ufrp9h7r0lsfc2rmzmp6xjm9dhzp58bl35b1rql6n7duikon3la9bq7mqtxqznb8doawrslwr4j0z46tav8wbu2rpm7km5kz1l12hb13ok2v7zeen3d3v3fg4tab8q07avsejdgqsu00o43zf2cb2juwtoevxczabpylznt6ygyb6wiip2kye4znyrmiz6jiyh8jg4oduknrl9tj09od74zidm31mxjdsm77a87oxzyu2so0jwkjajd24zqnnqlinh0asajy59v0gzp7mtr32gxa12w847zn4m2mcuco0h9sd2oeayvb0jtodtzze09i7oh6ejy0wzfjzyy7txtobhzn9bkeisjolpa1vinwh062d1t5zp68iwcoe6sq31j3gdiu2ah1ljr5vsmt5s3n525vvr877bz06gohsuh93xiqtavi2gl1r94z4czg0nw0e0n4bm2rwocs6cc1clro31aytxxsehik6452y20fwy7ljw2215l6jom6zd57sw5smc50tg4msaekdmp7whe6ipj6m88b1nwf6y6goeo4rigqokji9q0w1breeod0javizwopphykalmcor0tisyqmerfreyplurbnp8x4n54thzm8l08ctbuykwe2unnnijouwri0zqe8ia7eqgplfp5570e1zmr063h72b0kdljdt3hpw4kx4jdyy35cenjjzne1v6jkf4vpipfvwsg93fyiawepe5elrzd62f3x6fqu4v2o82itc9o6wh5ilgg55gyrmrr5u1kk2z26s2xlzdkob3wklgxcsk243b9l4w3gybtm0ty8w45phoq4pegzvy9gujxvx19j9peclf3nuxvy6lb0k3rhii2pfow1aam7amztxeac8lizigx9bvafcgwk7sdfwswhmvj1m9zs7mgr5sxntod7ic6o939lhralzpsvojbh3kw1cd01v0fkjpbn569hbdkex3m9gvcy50h5o24zglgu2orvfuqlecqs15u6b5rpq1xgatgbvzo6dff5pjpe2lk8fph06zwu1pblifiygo79rss5twxt6iiri9h6x1syzcy2y3i9eorwtrjcg82nri1cqb66m9qdx5tunpr0k0o8ip14m7f61udjt2sxuapxvu4rolladrfif1j2l2f3cgwspwnjyd8auivbl0rceu2ioks8pcfzf33ykm487wy1wsxzdoz3fcdrv9rpu6aoreok52dg370ikn7qmph8f197q26fuy24chcdad7uqmjahvjr67fzh33g3dza0nit09ujzue5ibi99k6u7w8f4c4b62b1rn8w65t5e4uu89jjef2lx5vtquxqdz54dj6xqbo1pra8243zttcc37246yuzasf5hax3nyz4lajtep1hz2229c2r9ua2eqsw7rvjfyavs98wqmcacue8viqeq2c0v331pjhyjxfemnk8y6t2buyy2ky4c3wla5n8zo15qw8uuew7vkpqu1hhw5rma2y0bvgj8gssy28iegjwkpsypw396t1h5nr51dfvdek4pp1njqfltj90dl0mvfcc2cl7ryhfm8ux95u1nqi520mvd9ft9704n4l94fckeg9r8719xzn06cglxpig27juelvke6bh32pbu8kavm9num75843zasv2hrfpkj9flttbm4ur5l4l6wdou99717cfo0v8qhpv7bvn7gldodgwhqtilx0hsiq040b4sb5kkbuz83uopwsojg0yh6bz8qn33syjkrxxyjd5re7jyzy79lzs2spqrsoofbiupbdsb2s5w15unh4qfvh4n9u7piso9ky5iejjx4h7s4b973muh3js5s9ltzs4uvsnhnxpj9jm2mq8747v0nbge4tci0jy0ozcj6eg9ngv0rgjl2ijzd34gwvuotmkk2n07dif6yiyemena185cf1sibq6bym13m8g83rmp1yecorh18vow1aztpfpgde9qjg8mc77k7og7hi8o7wda54zqtb4arcoibwmayz3ghqjhvwqgx1927gqwngyerta551s3p6pp89vhx4lniztq48fntym5syt14xv5nedz756c0rdpk1hlkddol3kikhsh4sftjtr0i7yx6uf23mt4vywcpgc2ofowvteb71wtci2938duivprbp6zlni6d2r7tftnocq5r8kak0ltnhpnv9gk10k259hligezsxcij9l64idljfzl55vlr286rmmwyyhyxjj31h4h6h42odxz5ot7v17porzpocsorkdhkq8h57e4obhhjxm2h8qqz7lrep1e5qlxi39pl7s77o0ajg4ptrd5cj9nrj0gpli488l3usurmdqdbmdvbuzonler0iqog884n6rpcy2ivl5sqfsszwmnw2ge27ogkbzv611ojfy7qvn3osw5qkmz05d6goy5r69gr4whthjzofvf229rythohcqm0f1zquskk898sgs9fnfizg5qutbvkcsst84rymmljpv0l5r6brm23ytx7ltfqvah2y4y8lwzp90zwhpez11sf6hufwxwpfnm24ga1tynqqp9kjprb91przhfh39b11wlilh6df2m8b9h2a5yft096eb9c9w131ib0hqrrrl6016km09qwo7ts0xuy99cqt3gf6mcrt3ucyewez050nv3qxbnb2sh1x1lcwwbsknfqlbgteeu5bmeasyb9k4cgit4wqckdl3vl9a3ivqmz0sqxf1ddbqf00q29c2ej4j144i6aqlcjin7m9yixc4joachw847ioam2ktq36w9wczrncm7xp8d2rx2d0t20dnaqpdv9xmb8iagc6e3y3zo8jb7y5mp2jzoachi2gvqyhokoktct1n7v39ctd8ngr6y1aaqyi5eekkwsck3ququjekiypx46b3xwnsikiw5sfs5gev5dp096gqgyj8nwixzws1735zv346x81qjvocg1k6w9av3u4zhvk9j8o4psxkutk4j5a8gdgn6nc947al053lfiv4s0ie3iyvngwcl0b86kbh1kpzz8fcf9ad9wpgwz1yb6w5p9zb7z4gugjv7n928a7x0j78fr2gwr7jqxgtr2bgb8ybyzgt0f7qxz5b3nvqck3f8cdb8q1i58s083zgqkd8cdf8suotlol8zluq1kvbjlhp7t78x1nhdtnea1ict5nv3i5adr71nlzdsj32sr5l7fuvldgpjgewxzdr1ibf71veegb6asoswy7ntx038gm7scl2p78ntp5vahh8eu6d0e8xqcfj2k34l8kc4grxpbp07jmxdw1vidumwc9hiybvpgslwrsolobv5z0c3yrfyt918ff895uctwbzwpiv2a4n69514oua4l9shcyj020v17hajvgzcq3jnwin9nk3tvpguppfb27uj6owm71uotik85rghvkcyh30wjrhmm4a1wknoxflbacarkd4yy5e3a739p1mh7macjj3m2ls5p8b0n5rj5y0dinwfu2j8oc29rqpklrh953s3t8sb4aoodqo864kpb0u4dmdkohwuvgyj4ej7n5xn6h2uhfh3qyela9pkwrmm8dr0doogv13gxee0rwiy7ch1md4ubs17hn9ecis8feslrwcds08kz7pxdl5ribozedtbwhe84xckl7tx429ltagb9jrd4y55zgr8803hle0s27208wppxi2z18w3i02fhtl7utkf0dmatsa0ilg33bq62xyq5ihfatdgzmvymm2eye6x6c68reoq5emvnu58xjc324ffzuh33c5rqi2qz9rgn0vodwqdt024356ixvb8ncopnzhofjpulvw3q1rd2f47mzoilb50lll8v2rr9kqhv1kx9hlunjh431qi4ilzfx9mrffkfd3uqko1dg2istxv9nbhv8ttehh1m1axh2ll4l26ek8fmajb98mtlrkrsku74geie1yvhopg5bzlceph2wsxmpzhrrl1p31yff8j7wbwi3k0098z18jzuaq13c60daxpja4j9jjk11k8evj4hm6ew44ey01yp95sn7kh19cyif9vbkmuywgy33lqokcrwb38o7258ar2moc7aixvgbugcpomva61sxewjd50tu54vfys57dd0ainla6yb3irth9idol46dhxz7b3e1dqasyrxslqzo0vie3z9ai5sbdbt0k1dodau4jdoo0ozr8o7l6pzrt93hmp6gv73nigp84cz03mut2k7sa7jmcv8tzgwd1d65hm7plsq5vy4c47q80g9hfodhcobctg5u778atlh4j5thj1l60zvf8squ84badptvcyodpitsgbbggz6ql1x6xhvjjhvp8liuzs2h64njosfwyf10c4ggcq7uswxmk67zy3qllbmiveys92xdrcykzkzp20nkxjyzthzp4r7h4qb62l7d1h2qfoyfkh1blx531upeyyn5k9ng8dzty9gcqj9g2sk8saxruoslcv7379gdnl20sjdzlrqflpv12z2iucc856g5k5703ggs3e5ysjtj9yzy391qophly7pw1t02z30asvwsg2en52d4aj94qq9qpbhtcv48x6fnugezruks3z4dta8v0uv7rxxkykh614vy6hqxnuipr98eoyaswscam0hvlfv4ceqg9s7rmr7fs9kkvhdppr8bi5fbneazv8ca53g6a0tucnr3efq0om2fxrj1ym4r7jxsly4ty31olv89f9apkx8kq0nxsud60mr5o5yur08tn92u0do651o98n4pyyzmtzrniyag68wuwwgzdnshslphozp9ce0dhjzmpi5w76t1nhvelm6gwg031agyeumj0pka8xrfllkmk3afcqjrx42ojbg1ejz1w5gep07hefyzh1uz6defudb7t2eeasrfzwid908czjtmgakoprzsitflkni5bobv4d8jg059wm88mrgebpw3mof1oezj9qr9q2cxou24io3yypou74edc8jgxv8yso7r0ykb4ys7akqtfwd1c2srxq59tut0f0vtmdzibtllec0emhthpuq4tjuap7xu75jren0r26t6bnqus9twap3nir3jdtvt88cwax5cuxhar6i4atj038h0h1u7mxiaxq10936b6usy5koka3y442z4erx1holgj75hbl2oj771p4v3jiy26ptymhe7tuyexthaefzzj2ubz2be4azu8437ut3qgh2gutiovh2u491eplavvwuur6jmsbdn4fpk3iombnyhzs4s9v7itmnd7s980wsnnl1g6t41m2to2mlbzno7ud5ejme5ueowhoad4zycpjoprvkl69mzeq4xerwionnh7dl6fq8fn76exv2px74ogy42i2qfk7grkf9vcpv465re5h1h0vq7a8lc0kmcstprg1jbuqpzcsa47i5t10ievuobiytriqm2zvcbdx2w66g5au7zgatmk88sp5ef8ct1vtn9dukupj3jqc05h0d5lwt8hee6rirraa1od6n41jul1rlndev746mfua5sre2bhb2su3xeoh7biaflusfvt0d7y6bno16l7gamxhxs5hfd4nttavhdqnf78yoz9yxpeimejbxczpdbsieu453hncrtsc02c7qdvj1otnkhdyyp5xr7o6dok9epy8bjmejb8cwkexejeobd87m6y1vfnatan3k77xq7l8ud1z0smmq11bmu0rkl7sa52d2uleq1x3nwadjl1mcemlq3u3jdd06mzopz9cu23d83d7sxp8j15jmup6wkr8asswxv6cfj6kx8o2dfipujd4kc6n42u7y1k173ndihms3qat6f3wqagwvw1vlnrwkl8b79ojk8ul2479kxogw3vz8fq7498k5wi7tx681of67iey7wydlhxd4l95nwihj8mgj0m477mpvjb1ait1jmcqab9ueus0sy7zbanpcdo390637957554xl0xg7e80a2eid4zr4cmw4jwzkm0hobdy8iu33xwwn4kxr0t7zrdboh3h6zsaaml8pdl0biohu0sic3dwvsiffptvp4vc3742i3zf1kdkwhfn2hod3zjvyqiz4jm48qlva2goh59xvzovs0lbthmtg7glm3nbv4le24b53n1u5izgtehr12xim7wr6awowmu4honvvhlezz8epallm9qcm1bevxo13x2erczfop4mi7ifk2dyt61y0unq2m3ngt58q268hxfn3ceugxrq08hhsv1a0xmvzgd8ieopxvfedtbgaoralfhocplcn8ou28btxwliikcl6znox61bgzy1vsh8f8ylqax878aytlvwa27fn8gnihpyz8xez8723hbgj7imepi7ojc4ohrqd23not61pd2d5bktaw1mi7e9prblvjc3mvt899nswq4e51sxojk10ninhwkzdqifeshqmodweiyo4fosd8spr40epheygp4o69cgrsk5xj97gqgmbbgi6m6ptkaqykj75tzvf3znsuylhff6lmgbutjbont083terq2k4j0gvftmh6phxjhs6nadbkldhpg9er8smkb66dbh5ebevemt8fmn5ujewlspcv4yqk0lw4z89sul1aj7a800ttvqorztzf0tgtbcg5724qogx76xod8c692o9mvj5vihqwzbl7cexc2wg9ofpdzyegitpdgmtsxi25c9gj4oaaindpvbqr1q5qur0a50jr5pklw2k23sgvt4vlq5uinxvoozndydijs3sdere5lbqbk1dkqryzx5z5r573781mnlnsejij16v7ke5cyp0f0065cbty6lvvuj47np4qe17qxt61i8vynrux4ma8y7m9x2jl60je717dz6i37qjg8ojv9qpp2vygrevk0otsjh3v4epc9efxzcx9biy67u7hujfkg19d3jas8266ogjs0fwtv48obvm072ymoz6s20g48eddm8w7gl6s076kbv2zc2ivfmg4jk9nku8wbysqp4mqsgxr5k9xjz0fq1knavfyv71st597txev6lmlxmjx2ahfa6uyfzq7hvqhqw53byygxm4gtd791w588xvnavi2251a0eibqt99lio4t9cx8myh98nqfbxtzch24vqcg63g64g2zln8bdcnjngpvqk03kczonmy7jymhgtkybksrirp12r1te5vc1lvhopxitnm7a9inlfmzg3ttnn4l8n6b5giodyc44wwsl5w9ivpr0xnusj0cz0qpfwtk9uogc4nrgg8akruw84zlewy5dwxgv0j3t8x20swyillbd3js8on9sb1xv6i9sgvd44qvdc70cvo16cbcboqj7a5h6ngdh414dei2qif6dcapw3sfd065yrrfkqw19nn9x2ysjcn408qwwlnj21n0qoglushbxfz9kpqnvpah4v41o3t12gb76vzcj66bpchu17s081diysst16luxn22vhzauw2unwhg7a76hnm18hksug972ssahsv31i53z4m0om2b585leeg6i4g8lne0ejhhpwvgthhfhefh4vn1c9a29sjtmz4mlnegofqxnw5c43fu48vggd02u1uldww3ws6tra5j6hfp5xsx34zzitt7td9otg0y0lkf9cttptfynmgcb2vundan6vffa6t7p527c6jxs4bwdgg52n802iqwrm883b3178xmnioljlzwmv5ctk9jotn5l9heytxpxupvlmhm0csbz6ewljrap4iqo7qub6pv773onjulaurwa5z8okxqte0n1xsrz36598z8c7nb6tatrzikluw95c57qv9hvatos5k5a2h75rfv0eysyzlysnehyzfvizaioj5i913yurkpidhk6go94ub73nb587ijklxht7jenj43cj5e00059pp87pyxcb9ue4jskuiftf01o7r2vr50aroz9vxi4k2o2onvprlniy9ejhulks7f6v09d1p55g89fh8fjziim24yk25s54ulrxn7uhs1cra1m7afcd2afg5vsahjvzy7ygiicu3koy6c1pmkn8ompjygzz3e2p18tjrlwy9pxyzc0424sk5iei9hmtw7c4jey01b4sajgab6s7r80kjzjtj724vfnpor5tzc0jo9cwxlo9fw91w1g6ys8ki3uoobf5os5496dhns4eaeetb6bo3p0mw9150eye85ew71okyjs9aq1ax5ffot5zb74agk75ijvnwuvsq3mhlptoksmmq28mdlwlp938m357navy2a7rn9l0m1pey2zcm4py75hz83tv2y1umckh0hd2sn94x7ajb2o8b2hzd4zia1ektow9p5fbt7ldr4j3om9il8tqs4jm8y6h91zrde1h5lr6lqda3ndfnd2kq76vg3denrdftpqfxmws91h2fl0tsk58aaxkpr4arwtm3xhmagyrrilqd2wd3q1rt5yxwl9h8c3a7yq2nbxgpkpflfpzpjxv57mwyykliyu3n7jga8uv3zl2b4os2njg81lx1nqsfodqtqv1ew4mgvgt58g7u9kxzkr9z9u2fkkyn4rglhq7hwekui7q20q8tkqhlzh0bdd77tmd3nx6d2ta507bpurgk1qj1p6lstsftq8rw7fqhgogglhjx3mogjs7o42zv6vqvby5q61ak91dj7uglem6iaga7tl23ng0wwh9yu785xbcvgq8e90kwf1biz0jgj233mgn579pz89l6ehno1ee2a84nvg4qb3v9ej7d25yd5pwi2v466ygrz7ymki6mg3w90umnde7ftrzy93quu8vz15m4jboxbmt0xugsxt3iu6qhbzuc7szvtfrreib9l4m2wg7fbzf7nzb0jjh8rdcbz45lpelysziglc1o5x3b4vrrjxbq8msbgfqmpbdpkhw80k7sxdbriv131nnnuintfa67b4u50chze0y3lje8sdsionlgwpkkunadiqlb4u63q3ybveh72xr084nv7twbjdnwp80tlv4hrc82ip8cv38ihput0h3or63ik1zonkztt4ksvq7vqvt6xo7ewn2993i5n2mk0mr9ismfylmb646web2gxdizpgbbcmut20va88niquklff8az8ccasdrausjm9ilf3l8pyzvoufanfjkn957l4b392kprggbmbt98mg0augyy8dwu9b0jqrwf9hbbhavwi2htoo34pi5vovdbulnjifvbkzbqraefgwy1vzsbnfzqbxrf9wj7s572b04s8n5d9ukrond3ftek8wfr9nvi8dvb6q6tjb8fayg9p0dc0pjsv7rnjmt210c9dq36ibl43ekd5fj1w8z5anno7esjaw5w9d22v7kisu38hz8jotxjx1z8wkcpncbrtdohli2sz8i7ptygfhbotbj6cer9b1jikjwstyum9g8j5x7itrov26q32l6jtd5xtqui8h3v1nwjb9syb8yduco82yk4lf6jibv0bl73p4qqyg5ah2hh9ljqch9ziswjob5cud9ysvqgqt1ifaix6h2jsy6myx4jy4ze7at72l5a7an5fj2o1k4y9b995aqiadm0xcqa4rnw2efs7rdoivwxmvrv3u3bxw6tj7v4yp9hyr573um9phukh4lrifo6ksximtggo9p8ikes201858yxcqibbxbeegryp400qul781ezgqwe7qc0mm5hapr3vfmcqp6uc0t6r52trxr31tlpc14325fg4i7gs1pdb0ahxpfaa76wq6jl9emgsqw5so84uj9u2g29bjjg4bj3povyltbxdtkzxnwhf5zer6wv4i796sqji1ik603bfjl7dznm5ga69yuy05w79cj7bqqezo7pasberla7jx9i30lch5zqjdvuq259dbkht3kjsqgsgzmogorqaqj0gymjo6njizhl2gdfi5cfq527ry11m18my6imni2e2hto9enbi2n4dd76axgxt6tr89gk29ifbvhu9m5lxhg5vmyk4js45r9x4jaxim6kneqdeerapcfhi0wh94it7n9t7lej1sr4mbr0esauwz2ewgvl0i180begxjyt39l1cu8jhkgjucscgv6pfi1ukxoyikjaqmja0ojgho2dq70vpxw54t4gv3kwbiysew0pgdfozrzepr7x0eyth9rygn0a82ho59m8ykhwf6wpk4wr7wpyypkv1zeyx81wrqx8p8snbx6rcjxsj5rlx0nqqwzwyfawlw3h7dqkcqohczpcrcu95w7pzanibdo2vnhlcv88kfhl40oudgzuq40bv6w6af307cuifhe735ti5cl0y9ef1piu305r8cc5ib00j15gt7zabystocamgrithcu4p2ufaqjnloj6wserp8a7tow18b6pd9mobbuhfjzdw9u4dzumkqdv2r5yasgwcja4y125oy5xeuwpti9cbiik1vzv2lgb7ym9f56c41n2zgm4snzb7bnqn56o");
        int c = 0;
        for (int i = 0; i < a.stateList.size(); i++) {
            if (a.stateList.get(i).code() > '0') {
                System.out.println(i);
                c++;
            }
        }

        List<State> stateList2 = a.getStateList(Time.of(19, 13, 50), Time.of(19, 13, 50));
        System.out.println(c);
    }

    /**
     * 实例状态空构造器
     */
    public InstanceState() {
        stateList = new ArrayList<>();
        this.mark = 0;
    }

    /**
     * 实例状态构造器
     *
     * @param state 状态数据
     */
    public InstanceState(String state) {
        String history = new BigInteger(state, 36).toString(this.radix);
        history = history.substring(1);
        this.mark = history.length();
        String reverseData = new StringBuilder(history).reverse().toString();
        this.stateList = toList(reverseData.toCharArray());
    }

    private List<State> toList(char[] chars) {
        List<State> instanceStateConstants = new ArrayList<>();
        for (char aChar : chars) {
            instanceStateConstants.add(new State(aChar));
        }
        return instanceStateConstants;
    }


    public String toDetailsString() {
        return this.stateList.stream().map(State::code).map(Object::toString).collect(Collectors.joining()) + '1';
    }

    @Override
    public String toString() {
        return new BigInteger(new StringBuilder(toDetailsString()).reverse().toString(), this.radix).toString(36);
    }

    /**
     * 修改
     *
     * @param start 开始下标
     * @param end   结束下标
     * @param state 状态
     */
    private void update(int start, int end, State state) {
        if (stateList.size() < start) {
            for (int i = stateList.size(); i < start; i++) {
                stateList.add(new State('0'));
            }
        }
        for (int i = start; i < (start == end ? end + 1 : end); i++) {
            if (i < this.stateList.size()) {
                stateList.set(i, state);
            } else {
                stateList.add(state);
            }

        }
        this.mark = stateList.size();
    }


    /**
     * 获取状态数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 状态
     */
    public List<State> getStateList(Time start, Time end) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        if (this.stateList.size() < endIndex) {
            endIndex = this.stateList.size() - 1;
        }
        if (this.stateList.size() <= startIndex) {
            return List.of();
        }
        return this.stateList.subList(startIndex, endIndex);
    }

    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(String month,
                                                                        BillingGranularityConstants granularity) {
        return getStateListGroup(null, null, month, granularity);
    }

    /**
     * @param start       开始时间
     * @param end         结束时间
     * @param month       月份
     * @param granularity 根据月份分组
     * @return 粒度 -> 状态
     */
    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(Time start,
                                                                        Time end,
                                                                        String month,
                                                                        BillingGranularityConstants granularity) {
        if (granularity.equals(BillingGranularityConstants.MONTH)) {
            return List.of(new DefaultKeyValue<>(month, this.getStateList(start, end)));
        } else if (granularity.equals(BillingGranularityConstants.DAY)) {
            return groupDay(getStateList(start, end), start, month);
        } else if (granularity.equals(BillingGranularityConstants.HOUR)) {
            return groupHour(getStateList(start, end), start, month);
        } else {
            throw new RuntimeException("不支持的粒度");
        }
    }


    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据小时分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupHour(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(0, 0, 0);
        }
        String format = "yyyy-MM-dd HH";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day + " " + start.hour, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据天分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupDay(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(1, 0, 0);
        }
        String format = "yyyy-MM-dd";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 * 24 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60 * 24) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param state 状态
     */
    public void update(Time start, Time end, State state) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(startIndex, endIndex, state);
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param end   结束时间
     * @param state 状态
     */
    public void updateHistory(Time end, State state) {
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(this.mark, endIndex, state);
    }

    @Data
    public static class Time {
        /**
         * 日
         */
        private int day;
        /**
         * 小时
         */
        private int hour;
        /**
         * 分钟
         */
        private int minute;

        public static Time of(int day, int hour, int minute) {
            Time time = new Time();
            time.day = day;
            time.hour = hour;
            time.minute = minute;
            return time;
        }

        public static Time of(int hour, int minute) {
            Time time = new Time();
            time.day = 0;
            time.hour = hour;
            time.minute = minute;
            return time;
        }

        @Override
        public String toString() {
            String format = "%02d %02d:%02d:00";
            return format.formatted(day, hour, minute);
        }
    }


    static class Region {
        /**
         * 开始索引
         */
        private int start;
        /**
         * 结束索引
         */
        private int end;

        static Region ofDay(int day) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24;
            region.end = day * 60 * 24 + 60 * 24;
            return region;
        }

        static Region ofHour(int day, int hour) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60;
            region.end = day * 60 * 24 + hour * 60 + 60;
            return region;
        }

        static Region ofMinute(int day, int hour, int minute) {
            check(day, hour, minute);
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60 + minute;
            region.end = day * 60 * 24 + hour * 60 + minute;
            return region;
        }

        static void check(int day, int hour, int minute) {
            if (day < 1 || day > 31) {
                throw new RuntimeException("时间不对");
            }
            if (hour < 0 || hour > 23) {
                throw new RuntimeException("时间不对");
            }
            if (minute < 0 || minute > 59) {
                throw new RuntimeException("时间不对");
            }
        }
    }


    public record State(char code) {

        public State {
            if (code < 48 || code > 57) {
                throw new RuntimeException("实例状态只支持10种" + "'0'-'9'{48-57}");
            }
        }
    }
}
