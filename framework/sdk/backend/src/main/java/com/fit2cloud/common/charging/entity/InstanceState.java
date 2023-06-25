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
        InstanceState a = new InstanceState("1wm68pwhn7z02tga2chnv7x12ojmw4c260a6016x7dq75sracwalk9jn6afzdpqcd9q69mera35ufb4hvzu4v7ucmdqj1wia5jctp75iit3cwax575krwnbm9r1dj1kcxnbcji23ky10yw468u68wfm9098xobgp7etiovomocq1x6x389kda234vujhhuftsn1whk7at842wmitf2zhpegq4nauxqcdc9d4nfrc7mx9sur1b525dfeuw6aoxje4fbua82v56yy591doyis3x2jtm2jl5fcok6l66nb96djii1ik13aknsakv50f0ar1jhxhecipl1py7u9bfr5ra6gp396f31f8uul7kaedcgxv7zjnm6t1w0t6x10eynol24sgpn939kts73yl3u4ufo26oz2cbeqgraqii9g4tzjek8ugesgjz7wfv7qgf1oybifvs1ahdn3p4cfor0cfuxbltc1c3w6rskieo1kzqlge7m035axoxy8hej7tq90xztfm3g4ibtd32iml3w7oc3k8ztnsoer076eokpj87wspm356fm3nyhedw3zqcfx9wgktx1indiewet6tlch4w88vtaunnlv6n85yefjrcna1rhn7rqafygj2tbf1hvt1rbmpygp970geg511o05fdcgp66m8u1cu8v3hcokndatjd4wxuybld7kidzl9nsprvsbqmg6zshojy0gjovwsqg2r411p5vllos9inttj8tfqqyh8wjudazwoffi3kysvjznv6oecmuuqxjgjsh5ngcdhqzct81shj9r6lbb3r5c4m3s47sq8mmdh514mv5rw1awk52zmrtlafugj3w59ysti7qoshnelb3kl9dszynw77aux2p67y4vadvtg8knbk374qeb1g7g4ubgc8w1mf1277l2l1n3lje1jzxkv58lbkd6nwoqq3xsrx6zcu47n9ua1hovy88ct06zaf1nple07aisy6zbwyxedy50oypjowa7tybrgiq0ace1s459fmaj8bt5u2hk4djnxc0lfal1fxka388e1hqla99bndl639f1bpdmyhk4usn4b5a917gx8v1egjcfsdxqy880aqztgb18l96qaoi82k27k2uniu2z2zpmie2d3wdggubpsutxu0s85yh2ppptfdwrj6wffpl1brz4yi4mtd3ep7rjckccmrl4cz0dotayizms3a9tyq48a17m2uz5xuox6pw3k6p4gj0655mnsbkwzzi9rcb1yufj9zxizswjnopronuzjtl3obh4zlfjxsiu7wcmcoanf6wd8yclzhlpr6pz88zx8tytw5n3rxaaguyf4vhl03tj4i9gxzqbu3z7h6ypqtqj9crskufj90e4v845mb7pzew6k59nwr6rv6uwlbxmly9fnnetb0h1a7xwd00siejvuciiuo5ob8tgt2671o5rwaq7jgo54e4c8syz1vx8547t9xe88kgr9zszrvy0ocg7d5twhfhtkbeu1eycip2mbvag2gm38hgefoyfv4d860ln2mlhd2cctuwq7rjwa81ghgw56x4rfmbfai1cygxdajrg7v3ykmip0zjen97ao6im00tayr5ssx8bz0av3rk9327w9x1j9eg88gh6vv7k87cu7tkcb2tjtsuz5l17rny5tg438bj29o4ze5zmsu79qxjkvul0bbfk5abx054me6tovbb8g9tr02jadnssap43jkalr91ewc9m2ayrkjppeudzu34hbcjlxwixg4oif0p7lgsddntairjd7e34ziflma5049augkqg9jm0s6jq8ff6kpzrlsjrvnk5j8km981nrguoivvowckj6vw00kyk07l94eruic59vi2wohz826xbbgu6seqaq224z1fjum1wvmcynsri8tjfx9qteir7c22jewretbzi92zvlosii19sfc0u6goqfprijrbodntipivosarghzs2tpj2c9o8pnurbldav68l0dcfaabuckxai5uev3po61kgtk6w5fysedjiysktm1eeddr3ml4hnf8ytv4k0a7lippm3u3si072u1renjknye37yjlh33cs6dkbd6rdvyxx5hi7nfewzue1vt26ic9mmdoivi92piy1wpwpxl3jay45awuxwejsie3939ke72gjilcyrxxm8bokj6okwoa2kh68u1f5z0o8t3mnbee0cf21s4gyoulnd08yi3q9js5z61dkb9wydvd9bthsan1iyi4iokf12jgyqwubi8klw758ws4wis18a5l3lbgyp5f468f4ek7b30hs8p6va7w0hn0g1kpj9xvrlgavyjxhukdfwgtcvz9ux3u13l82kb9i73d7s72qxeyfi0bx15ris8h3futop8ivbz2hnn5axkc68f6ni1xl5ywsydr1yy7bgtvhclml9y2h7owxi2wj7v2jfyvpvfhrmi6vnnzocantef2v45uy3wtj02sspvup2hymq96em676hwkfhu4o83evqipr73kcqbtohxxfuppsrk2gfu60f91rht51rankgluk4fut4os5pu7tqx7b8vamdq3njk2o331k4nb0kk4chqhzxbfpgnjbb8fdsuc7kz3lkaagqjzr823gohg11qirk8ggphjff295kfh03s2sfjw466vs74xk9zxtz2ef6tdammyktf3p9sn8n2gxmf8ywfvas18dfg58pjz6y3n64d4h5xlxiauic85rt5k63vpywjrvh9relfbp4a3okc6ubuej62psclta6p4i94bbmyoedyd19vvog7743yb2b2ks6m2w3m6c81z2llct64hpa3asjck4wvb5ut0m0zpa8d9jmsaqx3m8loxwwpyxjd4ce73s4rwkua5p0tmwtmuy1h53u3yk39hfkcv2stizl70blcwee6zmk42qx3aweow6x60py57ekd2b5llz1pa3izu1z681d1fpe2ql9f10pj7lrpnf76jvw3yv1h4sh3mrrjs1qzkw86ldwbq52r6gk7vp6qa11c85ojc14kf0ehokojt682pj3ua6wp80m5ti1mulzarcg2e66fvalclgiykhddvo23xgxxr31fht4lao95zna82p6zeeb2ttn717eu7nfic5gvuz3f4fguimd72jijvnqvozvmt6divac13giklafh7f5dy7eq70rxt0305bs3totlb29a93b862rkgmphzk5huzy9wc5tn9pyanxcrb67trqaxw9n5t3328adwblfdosrgzyfrhrpp09cicjimbu4phbe1etjsov80e3onmvsze5oe119oqzxwrcloc1g431a23ftmfps1g1kqqwd1l6ileer6bihg1avolwdtb4cm4sga9woyge1st454r5tdbfh3bw7zkb3as1540vgk3co8kw8xjlh9ssepvrqi0del9qmwm0lf97d7oih0ivbydfrrc28q2tio4amge5ustjvcp08cf4b3gy1sa4qhggh1jj6hcanzlngi0ykmxzxga0o7nwwbvt885jsd2vi2ob83q7xe5lpi7rdwfq4962mzk9gnp84uqtiiz6dqeyfkqgtau9j29xnoj89nxbww85bppew6g7087s5tu6sm9e4vp7u2k3080bkngj1brwl5c3rbs5znn4sri5oqzhryd29csl4n98048o71o5q6fgtovo4jkilir3ujhsp0qqrc2udzr4tnvoi1zgnqlydtm4ixtbvv7pofwmnzaxmpjuxmmd0eazyw36ar7w0ut12zi8yo3quqcq6pxqkfd85zyoqs512hw1hkm45x747pingnbebeqnc3zke0nuuhscixo4siecuvk9qu0goede26m297f7xypmpefiti3xj2fes5mf7740iudm42jpbm09f6mm50m96onrntdfdjlh0kdh3sci60ovmkvhqpwwnl9ct10rn1oxl65fc9jxwww13kw3vtiatmmpvw1u4iiuaifzzocgdsrcy8e0god8hm24tpqdkqujr71cgexqetk7d866os0hs17cyt73wqzi0eogwmgcph8h82rl6bfma06ithvhbvi0j9a5r30ugt277yux104hvvcahtsm8uy0l5feyivbw8erxhko6aow9f0vksbcp45lk7ygg8kbc3dlrdnbn5be3k0xlos827pwqd5u8zxkrsmhyehjes110bvux81sf4424ucu439f1263jdv9pdka8ijasjl50snsapocodp4lj899w3632ivourpvzv72ek8p0s2k759fcz7y76vmczts29ujyzcmblz4kr9d4ahwvubynnl2qp5si336hcn9tu5xyd399gt1ukw74is97qx8gwp1giuaklx7evboq1cv60wq5hf1jz96ydak06kmgnti4ol2jnlo4s3gnp7oxfksc5aan2xk7jbliz24p1256vririvoxx82q70ryliniscojevw86sy0831ba2d99agzx7a5tk5f9mea72scuur8j6fx3z5sgmr1uwwz261vcunrc47tljm51iyxnyfe6ffzyt0yr4s3wbrgn7fe3rkn89bjjkiwqx0e8lv58kkx2kfr90rb03wwtfdp3bf4g7gyp4dsnngwfi65jsdd70g3ekwzz411t6m66snmt3fo7r85t9s9hmfhnivjgffh32c8q76sew4ds2env9hsw6drp3z4p609ox684jkrqfkjg62185hm863sqmwuc19jbibwi5ofjmb4kszhuzk7cv7q853n0omo1n5uc67zq6bp7m80qck2c68y7o2snvd17l85wlxo5o2hsr77h0kmmqwsbcq0fk5mly7ivxuq61jzoz5k7b3g17uk3w1nvwl4w1em7x6ofpttjnsb8sjic1hu19nfzmikyb8za74f9522box4uqa0z2oxokjv8kzwmdpivn1rgsiz2m0s5gipn30puvzf4hg35chpjiioj4v0szn58gyrdpdhdz01fz5514q997wryg9cr6uktdpxg9aw8mpuupuqlmp7oesoiqt1rksgnlf8vdzxvnlhtwd3ecwttu6tmaan3ncsb1uo07rtxetwgokne70duk4shbwroxkgzr5x3ybxz2v1wff4jyfffa3u0cf04sjs5ry4up97lhzgu1mth0qdbop314hlz8xgrn5nwf0p6vlaresmqm4y6ku21azx2rv3xfeu0mynxdcht5sdtv3e7u32xvl3iu2ex3ucgmp3uk8iuytwy4gmpux7o9bx7v5q89jqplkg4ko5jiv2qq0vwdghq8nu613enl03lq47g8vxs3c3ob1wj2870s0d4pxrz2oexnpjjtys3jei9n69uwd44i33ftp16wauud5an5so1yarlbcvxr6ih4z0141x4zb9ok73g9pgf2lev1a4a4pri83r125p3v6wpt6zbqq9evw3fd51ojbp43389uqz2pzotbd362t3ra6ice474rcxfve3kohkucdgce602vd5v4kg16dwkxmd879qdjtkiz2wehonty43k1uvpdkh6cdv43udoktkcd263fc4nwol31tl107n7wq5ik15d6g13vwp11teboesc1a4fa91l1xergh4zeeisqffsfgm7ve5x86jtqm3m902mrxh3cwyn46wx729iwqap8xr5lnufh7f8n8fowcofz2o6w8ub3wl5uhhf6efctlh079c4fres5r8wtk1qfvo4u6n1jsoid1994um5k3rxvpoc07pgkv1owj7axiff9z0od7t4aaay5jg08pmlai6396lb3u447j6tvlvh6slbjpmua7xq7m0i96qkiea302hwut198lxob4h5s3q87f9vywbnkqbjri8whl1wmvd92ul93tw97xkv3g318v3ppbc0a10dngewn30hnpwabh9p4jl79dlmcaaon8cvrhmrfgksvxblcutcz0fihhms8q18pfztn4xgpeyiivxipr7pwdpfc9n6br0t4ro81t7fqdbeqn717hbr9s72ygkwbnvfys5n35qmadxrroaw9xdqzwue6r0zreccf8wgrkw8mjr111ckk81m9qmeyhor43pfri1ac9zfbbd108rbv50xiw6m40phzj6df5j1xq9dbsilpt9o4rweos6na3xvair9ok4bobqh0wsvw64o6ol55t2b5nt7nc679cc4jrg3pzcyoxf9jqfifo1v87dlyo7ogf5ni894tfwomjjr0wkqowrnfy7adfvnjndcr2o2l2il5c3aurzbw64zpnl2jnhhexndxkoy7tckk4yv41e9txvpbm5u74a75dgvicumk1nvdwiv9icq6golyjlxvsgdumy895zyc2p1q1p8en62912ipulw9ttii7th795ccfr4vdgc68hkarxhxjpe0bzd6jd89l4rrdhwao4eeg2ic4t8h6k4c2ofqxgj5d03wvvbv6a6ghdidltyz6epvcctqnq2h6grad2enfdk3dnv2j410h0riodopilanp80ifnpqlpqm16we6un0mf1zdyvm2xfp5yabnn6wsha01yrdftlv5ukzkpyhd04g49qjnhqn56w2khfltv07ohxfuyda66ol8msf2qliim6jqstacpc7crg8x6b0jaywz937vwuyqxqdjy8wni037aw8omkrxgnzq0ga94fgcfzi4k4xrl2zlejn03pw14rc6bn8blyfhn55ikai5aggrj5eveaye8a275s4cp6ljzvd1cw7739pzzl5r24rrl0urm9l52owf5jslwrmsagfg4pbb3k1qqlh2dx43f1pa0rg79aca42wtx50cqprlcv5wurdcjo3b72o1o6nwgsldm348t28qvbzs3puyo77dasjupqpk61rbfxl2oh329paw4kzfy9oqwnfyyst1863pznv4w7hgq5sy4zvf6tgk4ttrwy3gjfrk6uxzpg01naxrl7yuwkfov9lql0emph3vjd60l3m2zi92d8dflgtwzfi1qmoev9krdc69yyi4bjmqn8j3q960welurlsa16zhsoklfneobbd04w4n31akrseiegljbju19nm58pelipr3yu957bwdrivx7my7gbbt2a1rsqpenzc3vsorof7068td28yup3dpblrmxc3w7bipk7chqhwo1tjh5h5onjlb1zxpd7qk788rq83yx43jr6enlg67pftkqof5k802vaq90qpaukslv131dappkr1024fgb5shrc0cfwuisldsz0sasyp3nk1la5f0bjfbckj8qxdbobih34kk705ulebz4bvceue86uysxiutp4eet89eevl8af6pwjstb5npf0i6d0q6t43hysz9vp4wjugmklafoyziu3xvke7av1i8b1aovkrs7ju696cvgp2zd1kheb6j3fdw6agrccxlg3933n44pubuzdr339cg64hjm87hwlmmwmvl416do9m7fhqqigyvp6gpfx54ludsqoj37tkcjtkke4tqsbhl1amnvc96ztpr4hfzva87vzcrab6jeyqgxc5aaeyx0x3jm2ruftzzow98v7neqnwvdu191zsioya7doldpky9uf41gf4uot5b8s3q4bz4zn70688430gp7pxrgsrlxvn2d5p1gricsie042s401jyouga5xzo5jk154hseexkh5ew1wuj5odz6s6nnsiq1ingdrzi7uw0crimdkp96apj8q4a65i6uubj8aew4n4xh8xrl3dwwnoe647hkvrzarpb7pknskcoadp0mg37js4r0y7bfxr4szcl599fl8smegzj2dbd9uo3wriziktehqucdhpdolw0tggee8fcov2f52o1hy9zo91frow2h318x2eio95apl4q7dfcc8lzwj05sd95kbkszbuumhplwb6ggqvrb5uxlrxvc66dgfv4dxhfd01r3xgzanvnbrn7o3c96cyugfr4vtqu4hv3wkbhyuowxf7nb2uhbd1pda5o1s9dh0ubjdl8y269s263na391mv5vxawbthh0am3hn830gq5cxyswv3pr8i2l2hmeuhcz8lz4z1mpvuqbqddylhuc332ya7thnif8wbmwmpjlyug2i0hm9tjzlwql3p2dxcg8atgvvyqev7tt0tf1akxybxk4lgquf9jih03ub8x6ac9d1a29pk1le9h3az3tiwgphknrwuimakfq6wfb43wpr13gwjxky57lub16lmzzp590n9tpzbhmpl9fqngfepj1qb51m9twkelseyh74x5zhvw2frp2w6jfo03u22zsq730dv0o92qmeprzjqn6tm1tw43u12er08i9338vgyj9qxadr05nillack4fay8r7qhb43hgmcio7bqtyxtuit0krgdjynb4llhmqhru7eqoyfq0426hnmjsr8yhyow06amtyu5zx7309oyr4t1rfjooc1kcjdkj0xdkyj3h4sgda7jf44fg7dg3rcu2sjyl2jha4fhzq5tpw5f7nr4juj5xt0ukqxm7a5ofmsbz12f6iwfkz494yytk9lptlvs1ufq9hwgd00bsnvrwnu6cfr3nz7toilrigteof5deev0hbv6r9gal3nyjn67xuuk0pqwg236kar1gt90mbhwxbb44rys95bzx00p8w27pbc7jk36j3apcf1iqpnn7k9qfpkjyfsv8o7vndkm9cp3h3eur9kctviuuu1v9mu7ab86rr8qg6p79hcv1r1dwbtilw4bk16w6zfjgda0424siyk542jvfn02kr8efqz63p6g2j3bwu5lu3ackrxxhjfbz94u0y42k7swv0i078w2swfsbmfsl827unr9jpyp6cdi3ziaecrhrd295bozfx6xoskore3j3rs3jimyjibhb7lqpomjifuktzmyizbuppvmf3g238uxfrjx6cie72tlpwssf0es7yeli8ebjqe7mtqte0vqnn04b5cznw3uod9tdttq437n1js7v4j0smgdf7kl0n3o0cj5t7ftlxhmkitbo1aqkserwmc3lj0j6r2upgs9vh4ocymvy2vhn54zl45aqscmv5ro0phkz7y0bd95cej5fcjsk9111s23av9vm6cpnylx0460l8kl1vm8s9hcgs315afwdbfe9acjcfb6dv672mtn8qznyipggf1jan9nbo2lppfvla1suvldin25d2ajge6oub7xztaoez1024s7j8tcvjawohp1d7oeqks1z73fs0l1nm5dgrsllu6w0rrk7wi8gjbe8k3icldjgieim69l4xdi2gpml5wcgzi6suqgzszquwbnt1ge32qbbj8db70awqml9iac2cjzintqvxcompixh6amd78gancucm90gtoyr94re1pppjwwrlz61wi0r9nnzmsja37exnisvrlg3nndqqjgvvua8scjtdfe3kpbce1iium0c6vuqie505fef7s4uwfi7upgd5ad8z0cfhvgze0yi1ty5vx38hqmxv99fq8sgw2ztwcfoz3lc5w92w0s66e6vpj3pph7582tfpuv846dt5j41mz914yk3p54sil45jwabi2ean29hjfbe1xa4p9xldktgpr7ircgvz1ge5gld4czbsieno3tqwd95l76wia7xc52td1dymwg2e06yldvj4bhbvycdnx4wyfpjcv4bsq83i8l5m5h9214lfkc8sl6zrs3v8iw4vvg8svid72qy2gad3tmrsybk7x0j7wc28fvue2x3isnepk6mo9yfdxdki0sf8fuclkhw2jpt0v9evclt51nrk2h4ttz6a22k6klgqh77bfzr8m4hkvud6c4f7cr0yzrnjvfbenxum4jb53vh68gw78b7dpcb51s1pc5pe9udrskg6gmon3khza6lqtnmzz02a1heq1w3ucabeqf5uhrqtq0kvwifcfjzmtra7wufgkcrk1hzfu1748h8iwtwcyel7njsjl9t2ki5sd54l80u1jl8bff8tz3i4cg1wev3wutnisr1h32nsfzzbxy3linjqc29qr0m3yrho5hunmtmbp2axw2aiaydhatkt19yn4do3rabc80vf2xzgb2hprktrwqp8v0z2hupfg17ta28tfmxurhl0jcktz7m8pn2ihk9aahcwgqptbk8omqdp6fg5yklr4uh0qe1po4cu1tj8ahd16e3j4n85z4v0vmaefbo6ybx4jtmwoisz6pk2x416egcx85g5gmyozgllwphdozxtcrzkof4c0u5a4paiq92uh1ixij25s8p2l6tyqia9yumgc3di2s4mlkf4jg4g4q9d9oa7xeaslmg5bn2arckuyzo54255njghxgqnds43rq22t5p839skzgs479mpe0ywptzfwr2covfctwlntqemp23na1zsm2ijyj4vs28xv2s5fnkf3sjve2bkf0gcczf3y0x08zncpkjmf1q3afp08np7f05k0428uelmcpeqbdbe0dmku74senal7p740qzfls1rjomogm95egz7rpvcwp0llw07px6ui9ike8rmajupvvuw3cp7f5415l6d8dkpy0fle9t1tpo4ur9v63xncrincgjyssovkjaz7f619f4l9xvyxk4el6l05mcd061h13zwldxufnbv111p0hrja4a7tzjxptqxx7izkvp70ztakyjbyf9wxmgm9xt20ec620olu3mp0htcwfub4opv5pqmmpa03desedcd9dop9awhp7kuzavur36qcuw8xsu5yrfvnycrhniund0i4d0ucg74vyh3xu2jafb4z8dwemjrl7wqjwgcbv5ir435sywgopph7op29qyxm5y090nts3hq6iumpogicd4zfb3n7gtoinoh1ckcke5vgr8zhwi68nb4y7to1qwsk6yj61ejwg65c628icu03vvqegdhkcmu91h91iw7x66tzidbpznlqisrr1c67m998by7vg351i0rembstmcxml73gs5snf9zv6hhsakhbzhfr2lo4twwj461f1rhqmqscc9dgo3h4txuwkmbczhlxpknpajttuzrgxx0vvu96i1klvuk2tmjl3swe8kr706z84e296pmw6vj86pkzsgs6fa6u4vgmx80ncscpktt2tk4zag6x7u8ll780vxbw4fwolg6sbb9d1ncyppeckdxh74cyxtocodawzgiuh9mdxcmx193kfqikg3i42daihwcynsnrm41jfryrfm5emdpxav51qyjejumfblzawf0e77hsri7fsiccikjg9odelaiz3j6npijo6vzhsytgaaizm6uw9u58pplvzubj95qozvw6r2up3mb6jc3520o4z8kt6rz09tvin8urxhl78da7ibc9otziztfu5qdoaajlxtk35a5oku9bn3o0mqw1on1peumcw3dwcz2h64j7cftskr3n0516sez86zy4e28qt6bbo986erompfwu34am45evnizu5ubyv9pylmvkfnlkhjuhbuspk3zlczx9xuenjncai5e9iijfmed4qe5tqazola7rcbapgnf5ezlmeb5b8w9r0cuu43nk5tt533ukal24r4x0iw93jrtd9got7x360lh05ut24ctzjo5miun71mb48fwlryuh8bdauyxlvvrpltoj74jz3ode567lhl03fgspgv6cbvncract1wiijrh9lyiki7oufk2p0gn0bxprgg5rwvdorxp04pwh7k4oe7ovaipxqf0mnvwn5kjpzo4md8o1qgyxcvqbljvtwb3icsczifueh44cpk1cfowv6ozbxdjek7tjo3bn9lcdpz6705meeh15kxr147b23f4qcqumzd392ad4ed5bj6x62rcwqmnb00hqgs9ld09lwa4u257aycy286bs8rda4l2dnvd4q889a98wtof46ggj21490tz8omdl80du3vg3hwk0nx1jyfwms9gzwbqkabf56or9vpekt1whvb5de3sne180lmsivewlri0kq0jgxlvevf0rq5662d5574uu48lqv0ioo9gq5pd5873ohljetyike86a5pgacp6t4ieldm0f35fbisrgb93up0hger9wnv2jfwc0t59v1od5pa51gjpl7y3itttyu0knmlqu1i0h53bjvv39g93nrgr2lavq352pwshemm2fap90jhu2obddu65asawosar85z671wl44gri83t0tjtesmvi41rzub4eawj2t1lm0knb29z0j3igs73hpzddyi3rdx4xwbpfv8tx2b5agdz5m7ucifd34u0u1u5qs55ewcceb21ma87nza3q6ih9as2m0fx0h5ld9hgryu0gmupe4fldp1j7q4bf0vdmb5yqnpmxdvwvtj8ryf0tqneefmt5wivx1q02789zd0tpad4gz3unpbnx2an8ozlg22mae0w68fjfycd5hp6ghjs2t1nal99419du7gda5v50pxztrvhr7yqj8k09pb8tmze007g31vfvyr30ow374qim2sh21fzdwavpecw1pvh448742986kz0ca5qg8iy3zh5a4tm6l97w464da484el4abu8ogf9wpxnqteosi4kuk5musssqmwcppzhfyxfv4l30tym0vslbi0053le863nqynhgy9aaxs617c04asq0pz0xspir1pgylneg69b0d44b5edikgg2v2t2gwj5fuoxmk4fz0w5z6p9gcn20xaszaswh1p954dpnidzjvf05lfikwybov2t9r1ruurt6mlu7kqoisdks8a9fai97a12uvzwo32aancs2iz41o7n02pcldqy1i949716lzg5s9d6bzkjwvacu2gtde97d4jxv3dh8wuqns0x7vj1tnlen09nzvl8cq979an9en0uwtrd4y9coedrwmz3kacvkjefqdsztnjqe19y18ara7ik04l1l4t9wzj5eh9bm2z3i5zf12mq5ku33whd3hrf7gg04vfa7dokd4umzi8zsuxhampt1bvyteptvd1qz0pg58ej15l2ieff7guojyoa872zm1cqx6bef3lo34xylufewtp40ku3u26rkg4q50i7f3ae77f1qtapdufnr1i8d3ttol3asoo4wngwvfg5pby90c2469oosddswwikj3tk08feo1ef537wq4qjsao4rops5xhxht59kbljf9z13o104fb2of35s77otd3kfq5l6h55iadf8xg64y3fdfcp9qb348uho26vyelmtdyn6qmgkfsgeyk5pqbno7bm1mmxx0fr6zbj6mt986su1jr24bfozlwawksnbznfxh3qqtwr4dh47ekrvp2entxr6ue6k3n46sijf3lrgquuuo2t0a39jhkx402an05c75udtqo7dsq10kjpcaw8cmt1g0xkghk1z2pumalz8x0sxfgfuas5aahmo1ad2fiipf7qz8s7zetqndw47vic9zt9wema4lmq7c3roqx8tjiognuht65ince6h6tgqipt5p37ex5xdmmhtnk889zh0aw4n9gsrvyvd2i1haw4ujr0zjtsbmjkwltcbdq20sow3f6ozrg4q6rlwddxo3xpieuyxi7yru3rtzohs0k6vvn1scfxa3lpy8g2f1quhxscfz34crw4sqtfhz1z39tn3rr6pcc26odp7tu0i62svwn6l5kneljf5ibbufqy7ovrqqnht7ii1vfqgbu91zrrjx7y2k3xr29lr3dlxym8a0w9sx71tokmu488zx33k0up1yk95ue14sjomp85bfbzf4ff20uhnk1e039f1g3kl09mx0ltrwmwssli67y446wmajh3saawjgkcdroncp3k2ivdkwfcumb3s4vs4pexb4vx01r02vug8cyf3w4vzfgk3cslod1d62gg8eo7h6hnwhidhw35qs5zifja528mz1895y3dexrzwyp3yzf7cekyfvf08y3jxz9ll13kpllhhmevsep236px1xc5cp783ecln0zws1haf5lqqxmwbw4vgp33makggosicqlnjki9o8zn6at9ldvsd2olf31fkdoh67rh5mooqiooudetdin6rv5v6vmzhzuiauxdvtssqnugzkp1njn4kiqdnsh88bip2txk8hqwl1j5t9ndjj9ewylq1i46k9gsms9c5djn5qwjq762iyhj4yd21ht6eo8uwqx9kysdnu9a6b5t5v7qkpu5c7s9ygdvvf28kul75dd3pfvbgjl2oxptavbpl8ae1xfzqa8k7ztyrcz2hx87oi109ync5vhqu4fjeh1kre1ibdx8j70qnmvjuh4okq2wnizinu5jgw1mkzmrn5xq5ttnfc2n3tf5ea1o8ryezikni7dm7z0d32v60p65zoj45tt7vdb0bs6bvajhe4twv6ys9zhrtm9lp0y3nfcgsdqxi2yk7kmffexbb47kbfodk55lyowmso5qg7e6m1jmdyv02sdxts9h8smoz6vgvqnvmfmva50gtwsnm97aelxr155pf6c5fxx991zg0z9eusy5c3zhbc7s729rqqq0inhj4f6s8130tirral16rrihv4k8h59reddnhz32bq2mfe3z83p9xub188vkes3fzn7fwybhdk41yl1pr0fbk31zgxdbb2okryn60d4390zdl87ppeba6wlm932zggguk5rmao74zu8ayptociygh0sbinbyk0wmotvg7u3vk3gupplw6mmiu24rtd8n323g4bhnzy5ijblal381lw29uir4b4j4mo55ghr3qot89jsgcjaa78a5es2dnf35tpsfw50kvs1fgwlmn0suaxwbieb1oejvx8bpjqsbcebe6rc3klsy9ycbwrq618z0hglisuidvnkww109yoygjj7pritr95l9tzupqr8hjttjdz41tac3x6vw2yfp7ks7e5pjlqynytg8okvnm2wujtbbeu01tcm3pa6ibkfk7irqr6a8mksxetxvlxhccyrtmalilmxv1j9co60sq76kmg6aniepu4xyndbt0hbcuwi34j08ot7yinvn9b6f5ax78azas560st7l2oqya5c30vciiupjn6wqnkmr17vtjp8gqc5o2zav7zlhfzic9dk5ve0rgtxzy6kx19nyz7bkmteqjftig9oq8jxru0o8m2xoywyphtbb035th5w2fhlh25s74ayno29fmocn7vqmealu3jryn4rca1tv1yk3uzsw3tx8qpa10e6dvmp205uo724581p97dqspvl7qxg081gpl64kf6ibwemg3sbkl63owfclzf41b46yzsxcn0odtmi9tkhdhojkrnfrdm2w34py1pao3ga75zi0wdp7ikhpohala0uybmaw9g0jnpa7yqk3vbze38m6d0o6tarved5bamkcaojd8ccv974hv48ep34hbf2qls0sdgfiqf6dem38tic5iu6frfeleal27396rocjm7q3od9nnll7k0l4nliiom2hpwsdx8c5ep76nkekjp5taru4i53fw7xntczcxoamh4yas3g0kf4n0qgvur8pgjiw18ztzajq2iwjfnik3sgij1zabnath8zykv9m7c78s0emg0z42xxxh8uzhy4wc89wsu8oqme2n52wsht5r2qi6bbassguby8qsby0octvd4mmrhht16xy28k509yqenkypx5saorsigd16z7cixaaoxdcmqu8jyigdcbiogum8pa8b96hff38re8xkjvvk97x5jzx0qby4x76lbtma8dpdfw8n1y4khgtsouogdjyd8z5y8zpxf4s837m945g3laqxjqafdn36lkp1ihe54euj006do3h21ntp4zv6jb3eelxnmtz1fd1lbmqxdcpc9pqdpzgxxuatkgoeomtu2gz4cjctxoe9px7tnx943d08a066z8xsbtlyd0wrlk1ifzgilt2yb2seehmvv7t6vbqpx1eviea4d5ioh0ejdzcayjc45jofwa4q6bjl0pi18wugd225gwkd741jepc44kgmyi2b73nf5kz8zdi7u7zaxltxwf73c93xvrj5dmvx404usy8bsu87ilzvylzk5g2tcdp8vefh3r0vlf4vozi0tx6eghpcxn8ws7x8uc3t8jjj7f0q0cu4xt9350niag3nie7wf073c7w6o4psctsrchgebiokk77hvf2h15s6hd997ns1wrdbzhtjn8zj913kb3whvs2gu18cz6xa0d0ly7xdruhfdnswna1pal46ngkau8jvu21ehmzjpb97gjy464luk6lbhtle360ovk2d0nj2tq8536wr2wr7w75bw0p0lq4dclcj519p55zjmnl1ot3skseu1qsjrzx21b88xfkqfrj1b7v8z5sdv0cxk6xme1ywch78rbnn6l7iooxh5nx8kz4b29tzwsurt10mvhufh1emoxh1msvacjr4km88equ83iqjh5tjmiayg3oigq0gy9025oouikmgjs5ox82kttbn3z1p1ch9bbl9lwf4yhk6o0ombg10tvjhtzt5as83nyt754lwah5cvdj3n98hy40gd77ur89wg0ho3cg64h7f0i38x1qq77xvb75xomxpum7f47mrg47xk43ai7w499wovs8hv9of3fmvg7y983w5p8ced55c295w9xvvo9dv9lzny7r3qx3e0k3urrqr3h9mnjff54kxg4xx99wsypd4bj6hrvtmj6edmo05vawhct2tsvj9jwvmn0eqkfhvmc7qrjql0tldsu51og3a1b53ygeqhs28sffq6qm48o5dfur4por2phk3gk0xc5m031hqe0qtpefz64a5fjdc4i54muff8hxk267hymg8xdi2k7hyojo98ayeitaw80p68wvvnlqz2sazvsjseewo6dlcnikmi335n0ffgpbpydg2xy9jtcmmqlrp2s0uqa5cvu1on37fon35sajhx0fyljw6pldqkezdbwnlgu2tjl2y8ut80tkjga8cm35rkt5ubhu59h61vze1yzpdaowp1g345wvshn288o46yjgxq52a2su1snl0mjf51xo6oakhgoinz1n7b2k4hpwkt3miqyx2fj4ts2vqpa86a6d4hld2l1vb5vfdnzehos0n51x9ur124bylkhjc5fqyypse3mz5l60vd1waybmka7v39l58a9g488e0vx8ix0wyp9wq683ilagwg89qse0q3no20ca3x7q0xiiz4gwsax0l6qy60ast1znrqnx1w2k01l8yl2ukatl79jve5mvketvirolcit76g7lcxhw8zuc3kezpzzpm3cg33fbf8ovnpoigw8dklaj4bwjjkcou1kgs3xj9njedx8rsh2i4f1bsk1qesj4dzmsghmm8oqijivxrym9gf30n8owiafz4nplhpu8s8ottn4ahhq64x39g7vltxxpy5uhz4vmhpzc8sqiw458y49fbnez0wsvfkj75i2qlxzqg83pfixojccdwpt5rt2zqwjonsxvdirum6wm5uai2kmkakw7h2gt6pa1uv03y08keavc92ezr9cnhpdkrqjr2p03lxyz7f8hxtella0g8pjhc0oiip74u7j971tmrv2rqt45i28m2ji792l2xkxtp7cynu3z0o6ecbcuz8mlw224js5jqvlilfsedkk7ymlptqzghv0lt3mqyhpxl9dc44pl0g9xz6799dxo2ayysqarzhuu7sbnhlhvx6p1aqpjx0drxc6qnovduc68p49oiijfwmq92jnvaxvox50ivfh8tk3ff5ft9e8tjh8iccfb1tu7gpd6ds2zkqua2xwfiop1y16xbgy5kju5b45ldcx6wrhm9uytqrqa37elsoj89uod00nwmph3rqu751qas3eu1gsixw9jjc7bui846ta9tifslqussjzaomno5uoylplx6839n5in8bv84a14300y865o0gq27bfpopjwqwk2jt9jrswa6f8w0y635zo5831h80ekjf87k7qpxpfgujkq3dvof3x4drrh7xxgy63c97o3dxe5c69qswg33r87a6tedhbk6kv7ynejz0lf4vg6i08ff8478ape6e144j4hnxlmsjnoeo5023y28p6qpsikdnv5up3mbpuimjc9jcqd6je1wbmwxyhej3ng1sk3pveyicd2jra75kxikftu4rdpqfx1m6kb9221a0btbf9vdh9b5yjdh86ikrr2j54vivpm9ubutyjiyv8yz2oxe40xr5m188brx1wilnue7lzw09t50yzfypq1vmxwi9dt9virs3inf42v2823x7httyj8gtey7bwajvnns2zdxw1vu1g49dy74rcshj31yl9fubmjrryf97f1qgkiqwv63sazvlnygtoukd6bmevilq1l65gc9wcqi9wag5frwflrom5tqrtmgnuvr5hglmjihjtxlfpavypftlhcjf90ighufiff7y1ke6rxwwc04fv5mjzku5f4o99nlei2diehi4jr4pgd9ke123dvldye9vioonvt5g7nl7mvgamnqjzqcj77gr2rm9pmb8l7l126nr06g8iknje4n0khakhtnjixg470hsumje08agv52pqq9ay7ulnhj9ci1urhx60tjet1blnqtigv1xuhq0tudmh5tiea0odqpewwfd84e7pv8muep37lc6xtgzm8e7coi372ft7otb5j4s5jteuq0r2ljap5zif9u177n0gv2sxnwdamwu4ladbxsd8f2jigbuhlsnf7yeb7q8l0tttf193xzodsi2rign6s3wkby5f14t4qj7zmufgw7adte2do8y1u4n31ud6vhxoqlhgjalpx8rucy5jihudtj9qcwtru1gnihh7rtdtschja7qsefxqj4erqjgn81d7akrdazvf5xmox0e38debd1wwh032o1e81gcajyhq98a37nykk7nwadmqfqai4ziibfbt8qp8exjiv0afp9balm4rm5k7yxdimec7788x7eeoppkntwy4vlp15qh6wqrjekcw71lqkcmvw728pty7gni6lkasq9we5ac3yxvmyvhquw9kwic818groxdqjh2z9czqu4u2rxcpk3ej6plzmv2jt17puug3x5veh04my9o6db0hhmu1l2bj41xn45cgufqbm13miy6kn0fbz0okcgszawj7m4n6edu087zjla3npr903dufq3qs2ct7byglvg819au0eri5odg2r9daiipiik8awybq1uglbszt9lj0212h6nq51a1gip4h7zn7odokewxad0iesarbi27wnlq1qg5q26xu4ihdg8t7a21k23jo8y8j3ot11xe29lxm3pk5rlhllx7e6y5t070d943dqtav10jof8oi7xowfwbkb7707nrqc9owdtqxcs3c0wf2gnv02x47hqybikis420d4qe5uryllir5ucm6u5zc5h905030p7j6iilg6rb0lelpuvt4ylgmv2xdttjxmvqskf4vvkm72kc2cfkuvwnp0fzbffm2b0tky47im1m8gpppa80jg1v5la26awsd9x6hijwlebgthpj13j4u5v149h8odpluhp05ievhum45py29cq7pwuhpqi454qstp2pp3u7qlawjx9s9g1cpmctic0zmskqqlpud3sg3j4jjkrgcuaxzotcfgwgzaa9lzsnedv45otqfjymhjj77jlovo5qndowp483krl5g8xqt7qjzgwyvv6p2j6icptd7smhqyc7jfuhslkh3vy5ujrrxqpdgadh3wvkuwa221kgrp5nxdes5lfkkqfhjyfyngdsh5krfd7xben30h4ia5q4544xmmcg3yh1jfl32wqn7fm4ud4seg4s1dzdd9y90o3etlpetbvxm9xeutqfrnpdyb560yapj8zib8zouxtkx9aw7jq7rd3jptxu3dvvsedy8544hzil8efrajh2969uob2u8n843uqmd8uar1k7vudhp1vq3evevckdeldj04s61ghrznihccomit4b0h845obnrm8jptkt9oeql1iius5nqsn75xz0aetjj1ko401vtl4thi7ann5eaaqapc56smceqatpdzup0ktkdigplekh80k0pw0lisee2z43m14dyafkmg97730u2g7zc0jqx2l0hg2mmhyj141c3o0jypuxkyc8pr5bfgrlfoxnfkoi0v4008a6qqq9f003kmoe947iv9l8j98zrgfns45c27n5y2z0oebm2zdyi858yp6ewu0wwtxxubeavb8v0y2zvl90w8qqa7eum34jdak54sblf5agl99h76tjt0485zyl72s6iw9wqr3cm68lnwfc9de6zt3kpk3j9omumocqu8zaa4hhqeii8hv475z94z5c7fro0r9r0r4gu9nvf1g0hnch10s1irzkygvwbinbf36ccjj6gri330nx6hgsfbbx03hi2kp365ruznng5os6hfgnasxqz6eddabb2xtlg00l6uau1it559noq04sfhi2bgpijvgeu4bozg3t6trsk5ooswsk8phsy5agz9nsdld8idamt5xo260u7ra0cfkgslstw4gj9u1inar0mc6i8m9z46q6c3r4cs2847fx12h9hz1lw1lz88p21l5ipgj2v8eblqbagexoao7bdh5iujfdm7t7c9vq2qit1rmlw4q7qagi86xfwkvds6mzcbeu89kuoh4tk40wj6h4enrobk4iaz1js0ctmewwedcfq7c013z2oiygu2zhsqmc7emujp3gagd4qjikm4jnrg7czn7gg635pzx6t78fbyd6290ljylvmjztolnnd6po83f6rix14kz3noqb9n56imqfo9qk2z3r7j9p664ylqpxbwstcihch6p0i9oocdsungocq7djjsfw7qkpw1bgf9j6othl46hroqi2629ykf6j18g7tfgyt5cz7w2e2dwmpygib1t8rwptwp6ryqtvzwzrohtqa8kvnl9vze54feog4imx88fow1jwy5vb21uscnp4u4axa9l0p53pn0j6ejgg39cz4307znzgl3o07iinnrh8sjjj8uo8dphqvasa7ifxhq0r6wkga7w071emk6tmfrl4gvnpmphfjgtdqva9roothm04ihoqro4pp8eoiysna80efskk9gw3kizusv6cgrdj74heufg45940rfve01zzmz0gn7puxzuo3a52s5v068asw6fst122b4ru204r6wmcccvna8cn87ir1wiii423nqjvttgm2r3vsnyqs32lsnbi2jnaoogr1jkrzv3d9hbazdo26kwf9beoxqtopb1zn9zug3mtl6tuwfz3vwc4icvy7pajoe3dlzs2i425pibi7vvirvh0my13hh61kbhb8thu31hshbt78eid63sxmemhd5ilrl8pk9uy76f2b3w0423pyil793fgyclz0182g9pinjcs9ujsvt4zxety3av1vw62m4btt5qjm985mtwb4210csk0ql5bvoy0torn12ywi7xqycf8eofc4vo4rcl3l9474o9gy6qkt5vq32ae5ccyudxewlfoir5c680gvi6jm4kejxb3nsruhj1zhlez56ai14bqjv2cwwgxxxqzokjpee2zv3mtn0zxv7j96lsfkzyfr7v56hbtu76zk8z48hkw97c0vrnui4aa75xivkdeoczyh256ntx7qpxtjzrt9m573v3apo33eujxsr70tb44lue6wc4luvpopuypjqv26c0rgnkmowygx045kgfx7rtcgh6udy5i8ug0x5c30idh2o7w40ygyd25nnl96l8rjc2z3s3k41zyr0eax1wv6jz9fei3oicoqbodm8b19hgjvxc17zw5f39qxiq7201vzkv22v0y3vjo25ir95u7uyyniquuyjjyala383h6sg7cwf8r9o0ud8mg2wwg0v6meew55fu0qnzzymzeedt1es2kx2xkfkmkfoel1fe6w7oxjy5kjwkw8xrduu44s1m4zk13t96oiru6looz9nsorfs50lcwbigif9oeu9l38np6htbeg96z9gi55b4wqgu24c3tbhnvx4m37u95gqkbuus32lmqqpb2049wwdohxg4b4sju9m4yk3cgwm3lzc3m44nm9s4cdn5oa0hvh1349iyq35u1fx1ysfkjk8a4esm9pzdjdlpoexvxrs4pfwljjpf9bvhb8onqykggolibnfs25mgn34kwl381fz8zshmwdb7eky5xt4ez7wz2b9ipq11pcxbfesg7z6ofnjypmncq3db4sxbfexv6mxgsrh1j1kjunk6d7scntnbvmg5un1ll0jhtri7dry2w00yenpn2nvmsiry9xwthjsniop8i23jybajbhag588j3ijopxzn16akopcq9784du249cwm0casrrm0t3bnt7u8p10a2cv4a0e1fw6uhqrwp7poni6pn2wcuecoyeem6fi6k5kga0xu8iq47prmqyb4jpi2izvuxv3dmxr82zageg9hop6sgaj2yw1zcxvx0vv1ajjhhn7h4tc90ozxqg35eqpauots4bz0m7j33h28sqfcz1j9d87ybkse1idoejm6w7jxju9j16kx8ekkce3b90e0huddom1n7mtwu78cjwt0vk6ovau86kcicq3gwhtudl22wz5rk9hsdce7j5zxbj7yx0hd791ivtw2uzyzwoqw4w7vw51e6wlm0crxcg9j8i711s59vqn1k4e46qhbq3kvrt3l66jffu27986modib4y5jqyfz4vz4z92bfg1ur5f3f75rqkyd72hzbv6quf38xnv5uieayz47b17s7ei9win0eplxqhi6z3y76phmcttaws3q8p8i6uuej6pmopbo4rirpmunw1rj5wai6rxjzxyh47gm6gm3s9qofc60xg8ww246cc10nvmbk3h6jd83akufom99qvtv66byhzl8r1c52tnbzxyrmogls97kii6vjow38txzaobw6q0x84q3dv9mbw4qdqej3txhi5dnltbntq4mhmi5w7mo3dln3n2o7s7fqj0li3ijtnt3tmympzmorv9427a3nper3tpsq0s90ek6yt4z596gc14sa5nooo89rpfjfwemgnq09zgqxm8av82ov0qggobwx0e96c5l1cvud85f6v62jhyobum1lpsxuu4qkckvh73y2s9n8wiv5xx940cde410fcc9e15ynwi7prhv74rrs2g7ofsqh44a4nju9v2cbjloheb7bfdko0xulazt0l6l2o7mgjw2c9pcwtp8a7fjwwr7z58j8gwu08zd7vkicl66ilpoab36fg47k2hm42b9ho6gyv16i9crxfhxfr1ph6pcnve632s9zn07sc3c4ndu33o895re4pegs8w7cjmj2cuf51a37h815taehqyrnjha5jq6640l099lxtxuzashsc0akjkhmsxx17hrvzvdwzwjvmjl6zcq0wcez8gx35nrri7x4my3wzug400ukx44ote6gfimjvhuf81june2i42d2ohdl3ya42dpqxwnnuv3rcelcqhffq8cfxcjz3t370z9m657rx4kba55x5anhur35afnc3h9grfhmm6u8cd42ll8cpplcka6ba2vibcp7lyqx84vp67rf8uit86kq9ddff9h7713zzghsl0qjli1t8kad954km08br5aftmm1omnascb95qh93qbpgj7zxsn3mbqp59jh9ullso3ail55qjjl6ko0ybnmy8mslanvptfosbk0kiuaq4t1ce3bl4znlthlxxlknsoemhws74q0gx3wqc96sby154jxy9kwjfahiyc8q58g7lak9e5u751die6ze4onnqjorinvykl4f2pgah0ki9joptp0ifek4sjg13n5ke7pc9exirtt6l5u3vbbdm4e4sy7esx216q2nj9uigsoo1ik9emma5fs5nfj3czl4swcxo5t0khurs9ujymb2zosbiuvmjcu77r30k8cn2tfqi3uvsvrt468nu9lz3jt9tkbtfljsee7xqeatyobu7bjiop00ihf1m3jboudhi2qkl5hej9nkc5cyxwddepufqr69o1y7crxtwj8g0jvs15rvyr6sap66k177ywy7oko55mg5wju811g714hutu701erj4tsu3iv0p3ru4e3rbl9gxfwxyxwhkbvhhfifgbmkc9lytg0agzw6afup14z9q5kxuiq5safbgj9yqkw2ywewzy3hg9dbowspc3cdbq2p85yb3i8zhrjzuropvj7ws071d2vhuy4jp5lm3d61l5jj074e7hg3uhw5pwyuggiljmejzrh5peg5j23kiy2biu09b4ut2pwgoy2l9uo2phoha9t7g0vv1f6thae95k024m094xzt9sk4qcbihla9pczn7ktuftsw0mvbgxys32ll1mqr2gms041o5e1gq8ibqfjoel1zxs60gpdx2sobhk5cyhjx1f0usixso6cnn66elt2u4opwfoxcxxxz80sem8si6jwx0mg59h5matyxfge6eg16zdb8j6ygpj33bz6853ye3yg2b5emhzadwa81z09tksm7b947ntt7hjwjaelou63v7ey7vlakymehud7eu5rtkxe9487ciwx11mx5ytxhqy8znaipwl7xs7x988sa5tq5if8wz2zz1nl8ozmeuny0bybu7uqps1mjtqebp4d3j98de36lvhsc0v7c4dc18is71uoeiqgv24bhfzac8jytoni73ia0lh3c7xda1cnzdso8tcyw9pzu2e7d7ou91jr0sukzq0iqdwp5vuicr10c7mnj7in3pcz58fpgtotkecu79ph05qgllxifbnh4sl9ieef8viawoux2qpij5dyxb8qcxcuo2p4639ao41vpsziekksazfbtg68sw2xjii69ia2mnkpvrgbh7zonjyttt4ie9wkry3nauu0fwgh9e4aqd1q9xt36dzofw417l6x7wxt8sfz5yaq2mv3rnd1u6plq7tqi494wr0lakmkgvkif6m1jte5e9wjbrv5yw69mdy3pf4w5idaaw5jo59c2dsair6495rrib4fo6h7jgnfrhumer74osnictlcd4dv88mx40qnou62mo7v8xmove8jjvg3wezyvuaxhu4pbwavs6tuaxxensdoovme1mkoi9pzlvm37huxkmehqe93ue9atwyuk9el3n1zzck2bp0649c4lke71ur19ikciymmfh3glsq55iabhs8u1f2z888kg3hkgpxnuarj0lu4p752cbqj08gsc1ae0qpp8ihtlst6g910llug5w8w65z6q4ifsbivmbayu3lk2m7e3nafr8bfurap1zinsmopdzk0yj8gw6danxhkaj1v0sazj99lshni9pnzuk9wl4u7lzjoyt5l94gjpbs51v50sun3lmo9b5u6w7z581je4gmc0v53fnw0419o6yquv41ab47g5khsft1k4esiqt69kq2384ika00j5tglg2203k4ahpj1iyvcgn4kkmf1ri4iwyvpxtsxlzj20yzo1e2fgoaapviu55zplvu24uywlmubbme7wwml7rkjyr9j27m7ucxhkk42tv2hrn14isowmy1lkcc8mwvrs0sut2fdwhfubss8u98q6yczh4npsi0t3dyzjtxkj3xcwtclhvl49n030mjfbqtasimm5nbk1k8sebc2d13sokggjhvtfqvu4wfdjr72spusmnrediu5wm8izi1qhs94lak0gqpy61eywe6nkzi98messuv63j5h1sut3jp57uq2usbj6a1av0owif9rcvlhr83yz8ih7acf7jyod6bzjbi4tx3rfx9a28hha134ommencxmzypgdxhvezs3q5mh7qckhzhf6qhlp1ybxget5zh75bkuweu3j62pwl3clerni84hcwjz4an15shtg0imfxf87lcka3kzvm37aw4hq2d04bpfguq6i3ixn4miedv2k5467yzbcmz3y96wsuam03kdbv70nknwlvrcafwzi96b2r2ujup973mvx9a105ily9cr7heedpo03sy8ml6fjc9qssj7i85oc2h2lqzf2mwnsg3vtz5603kfehraar6ru9y9vw8rh6jdsi1yuxj83p8moj7t3sfgm3e8gdm7w2pzvf3esszod3790fpm33qm938wtnoc0fciev7r1s0p4s11r7mpxfd0fhf8x6wg6rl51shmofwzk9qsw2ioh645xcw8ne5aqe5lngkpxjgy3n6nfp3ebjb1pyjr15g238y473am34atlc3otkw56llzl6u3007qcja7s75mxzpxbi9e6lr9uoldujr0v2m2pkw9kp8wpsocq4ku9x5vak3qzwt2ultygw9cuu8fvge0oy0gsygevmhjhpxdavuaxjg5di8rsgqgqwswgnj1u3oqnikot9metarovpka31nlc21xhszx7cvz5cx4n7fmnxco6om6lncb75xgqkvg2yzzbr9xrf60bt9d3s7uubcog2zuy11ddc9zrl4g8uttom2eptmlzq5uamwz81igcgcyjrlxbj5rhowcdm6hf59sbo25wkhn9310xtrj5zoj1mvctuycydcm1bitdvqrs69lppi1kr79tc74p7f5kizye8of6so51bsoa2sn3dv60470kb2cwqp5q5rtfkfx7371oil7944w0jrle6s2ao8fow0kaj86lby2avkdtkoukcsq922k4iq9pakapjhhwk5jngg2bjzhjab203k7858garkqe7uw4kc701m6fwfgjnvpkgs9h41ym1hylky9lprri4rqvaf9m63k2aqs2smwmlgby7ozikvro64ukcyflci2a6kub0t8ocm6fiac7ugxduz85qquk89p4lpz5tmcv80vg3orfr6yi17nbh4z7fxod8s9hgp3bg0to6ocwgacbk3h8ulejrflrg32aj80fnm8ek9jxvba4spk0gn48dij4yz0giq1gb2cx9r0zjwjflbw9oimi1r86fecvvd2vjt6pz8oc7xh9z2r65ihzhxxavkw9z85i2w828frvkmsth2h78awyqaruzl85tm3qme67wu928roczmjp79t1najzuph27oy6d6v2oc5tkpv3j0exa0pl643vjon5qvlmtlznynl7e4lqxqiqt8xbdkl2aajksn1zwm9go447ytncsbpt4npqptjko8c6riiuvqo44eb0uudaa9i1s1dzq8mw6q9l9yfq1oiq8x9nkbuj7ufyup2ia80h8aa7k18amjiggljiz3hvnm30m9nj0nxnxu3d9bytomr5zt7hjf4orq7w4025wo36qrar0x9v3w9wucu7apiluj3bdh3nztr07irq2y0hrcctmsplvdj7bxvzthdvzqhgpdntprvbioltagsoj451qac1qdh6a6c0v8w7mz3972kucke2ziynsuvk1iz7wn4vnvcsm1hnzgnyhsp9n9g1lh5hb6ez9ltum1ylylqe9bva0aqswkhkjz1mz26iwjcfoceumf571gysyhzzjeeunwq7biyvzo81eleq1r3o6f8em7fudjwvk19cr2if6rofk9a3eeurebp3sn8at93flvijrq96j87ir2atm6075cjput815fxxiim6gkr6ykb71ge8c4j6i6jul6m9n343q58g");
        int c = 0;
        for (int i = 0; i < a.stateList.size(); i++) {
            if (a.stateList.get(i).code() > '0') {
                System.out.println(i);
                c++;
            }
        }
        List<DefaultKeyValue<String, List<State>>> stateListGroup = a.getStateListGroup(Time.of(20, 11, 10), null, "2023-06", BillingGranularityConstants.DAY);

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
        int startIndex = 60 * 24 - (start.hour * 60 + start.minute);
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
        update(this.mark, endIndex + 1, state);
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
