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
        InstanceState a = new InstanceState("oz7bwckyu8qej9af82st8u44ifa8pgamb9m8uubahhjss63z7lcdjx3stgy9iiaebbgnzapl21hvfwt19lv79ligl60pe0u7ohjodmlwz65wlodfmoebiwvpv2xf7nya9mrf8ruhzo6729mh1p75x6ir9hrhfyrt8xs7alclt39nlg223fmnpvv9v4opy0ltyx4d53bq1gg0ggxijny5elbruwz7th3alkk7p380ut2fvs4yyx2690bamk1v3gqpy2fo8ixhu1sg1v1gjsc492zdpkbk7mih29w80oev9s4hpc4hbjxezeocwwajv0c1dzg1ehtd8tncycv11we5r18jaowb5ps4utquudkg3jweisr0yyvkvgapofhg24s6zol4vkhk897fijh567iqbh35r9hfhjmnqbjnlr81wdadxe5p4gohfw7c4p7rprjgujvti7g1w5ako6ned33wtvvo7mp13yycx5f9ubs3iehta0xorbx0g7sz25oq310q71ddw2eg4wl6onncknxz0kcx46epu575019hxrj6ljwat4st69vj2hku7f2bbbis1c80y2nfp733i8e53444nfkbe0ppbde6egcwja558caxeb04u2u0u6x5ljgu9csiuwwt4y2n2z8oclirabgk0yu8wklf9uowahuxbgh2b53qfvl1oj89ed5frofjgg0jyel9w0v27v870bvom2of4ujuox74mygazy8w1ef639cbnzy2fi8bud2rsiphwabjlnholiza9qf4qo9g57ya5qk0f0efrvl1d29bbuq8fwgb6i1cm2ckrxtyfupxa0ngmxy2yl6reprmo2mj0029c5gzk268gqdtouln7m7uh4bber6gyuh9i3qxtc1kl55o89xusgz50rx97w38gy07h6epxcdmt8oclw1exzx6evd8mgz601e16v4f8v1vxetqtlo5ly2ybxh29jtb5nnxmxdioh1jjm682a4ouanp2zje1xl0t2i8jqwonv1kyf0nzdni95muf1mj2l38f6fmetpzn6419w08b3wx3djbjq20amgk78n13mwrwi9mislcwsnisq47bhxhs0ltzlashb7emhouv94po3xgp2f3mzfvg895n35y0eakqnnitzg31qwft9rah396mpjjqdbjubrcxqovitn5yv33y9dei167b4jcamu7239f3opg4j4h2om5a7ti07lwsuu3ks1ger4v0veedr9rm58ko9izamiv5sjvwl38pk3v5r9snqyy35v2ahzzgfb4mnnk6rbqablm8ll4g1atervo344ymhiy54cdizu0yvgblvxhvvkqzp4ypzf0y7tj0y0aqigbxmdzsssthdqnjgbgw6p2nayjzhcawwveyt52vnq995zsb3qmvpqw5r08yn5g69i67rgjmgbde1h6gulrwo532tl43k89qwg3ypahi9ggmjar22oxv6fl8ih4eu9lq4ztx83kbi1oypdsaqkaa2rx1bl6du09igel10n2vd2pix5vj1t3ii8q4ds342b53aqtha6bxakc2mcnw2fds61a0dt6soqguxjfi23q6a9edk91qu18kgli80c2okq5g76s8q7ri50raw78ic7ah3plvvuu5hheu1a6dajsu2ha4gr0w1fm8lb88r20esaw4xxro16c85autxpf9gjy5kutbnubs0z0i8ktvih4vnxagye8oy62k0xtr85syqjv1qpgpo360dv7bamhr410pzolcw1nd1oatmcz1xy5m43of6was3trm5ti8ihkh95o8hvy4ol1q54rze4egoybkzhoobzvkapqve6anlk6vep9blazaic0hjbiayyvybamhk4tjywlf2tf0ly2jbdffw84pvwgj765d678loehw3i6ms0d0qreew5jr78cjdbaw8rxry8aij7itu8tyx6r51tdjdn95fnwk8x5c0cv7jvjysvqp99hkzrvlorfonb1iocqmw6ktuormr2d12wjdvi42onxmnqbcn405k6rmkn7521zmlh22odnli59iqdcuoq36l005p5l9t6yfvwwqbjhwurv739yyxn84fcd13jmr33cxu8ewwvpzwezl8z8vjtsxmd153swp07jjp894vfszd04tagbfjxmzoi88mm61b732kk62y0eprjf2cp49woa3wxeljnvz89t9ks5ys0jlo3pb6e6k5qy79hkdh2y8dta1jor3xa88o49glzj4tldgrf7751ox5xf21ih4bc8rs2zbkeccq03dqy7nhnvq0tve6edvdclajdmt706ndl3m69gdkvt6zk9xy1si5382hdh9qgyxw9s8ocbaoskewlgwkl0d0sxgikl9c16hrwkzo06hhu44bpzs819wwk00lerix4deio2v22j0up36yo616ne0naej6cw6z7qd2qmhatg4ymouc3n5fjjzrma81jff8pe8348lg8b2plzrzfurms3w6zkj41k3c98xdup40q8m2e9gm18zbygcjpvdgqn1deuya1ml4d4ovozo12qqn9a3gi0cnpr1svfqg591wg78opcur3unspe8hbrnr49165kri1sgqajmt37nu19ycgujljf6oqk3el5f16wp5s01gy6clmwxhf3zbto20vwt02u0verbax83iss8v562uf8n27pd3b57673y9gp21qlaisix2y6805rmv2c3hyxe290x1cubxpm7hz7nmjmaaj4mjjoe3iefasnre5n0sa1u0glwoo2wx895nysqu6ppcw9fxjpesrnjyyundkgs5nit7e67sa1xt1un45h6culcesu4sbglvk0n4z9o34hs1pd7dm5m1nqz32isaqv2txir9tg9xklcgrehydqmgsduqtx3flxqr2ucckzcql4ghh6zbnkoiokysaqy8bgguj259z8x9n5y4fbi34yf5nio6wfeozobuudf36mvzmln2e15npknxzsgmv0spgieobixoa889eubmkucd4l7cpmtbyz59qimpsw5ncauxz4u9ndl2ynh5ptt4tp84t0xwmb8emdqp17aajwx26lytg1wmzxtdrf6mwukllx1ui2qvf073ea86sykrrxebgsl2eni9urt9p39rp1e3ul2lbehoikq24i20mnc4vmf7x2mleqh7j2sy22tcr13ad96fa3z4rvawk4jfqazgkmurhbzeomr16j7ilquyj2g4fo7rgar28l4opbbdrzkzrwvqkukwhvgq5rjct6r9snc80tx3zvajs3koub51o828f847wv4wi14lu1lkzx9s0om9ca2qwiy1jw6rh604q991c7mbh6i39q8fh1pomsao5vb1uxri7vwxfsrbkfrf0swkep7opiltkyqbf6db6ue3zxvnhyk6d1dfzh4mq3nkxgfpsdu8nthj5pswg9mzoot3q4ivbdut2tz5zxhhshbj1f9x8h3bmod15iodq7m01befkyirbqza6zi1wzpd4nyy7zr5ve6uqx2mw89eq6mpphteu3ai08h2va9c28a35hkbgjul2zonsnoo9jpo8j8g6rz58e8ubflwvpy8b30hiu2fz22g4a7oewanuycz2rw5fzb2q62edwrfqgy6t08jmd40jods9w5jvcey9p3c6c6uzxuk8epui7wt9j7fuzrdeg7fuvjkfhkzjrj3fsk64x7okhapau06y83gwf5tpvcnbx3qpteul9yqppclj2viqsdqr200th7awjyf9p1uj9u8ayc26wvxmtblalw0qbq49f7eqxm0un8vwsctm5czgji06pggjio0t9wf8i3fgmaqalad1czqgkel9rswctfmawdgi4u5zb34ps6vr19ii97se96i50iosvr3s4tq4b78tpyqp5aemnklgdjs4sqyxikr61yeiq501zzeat1x4pbs95j9y78v4f8uoc4pmj9z701x7z0qkp88v1lbbrxqelyrnr4revophcnaygcsoqh8a1x1bntfyhdgqf6rpr95c7dquxm00pi1i76zbaqonus9zqlv1upzhhicr93c1gnxszkbt1u8dcia8dlp3abq6y68odyezgofav1ci6dnbud9zbpyb39w6uk029agvpa43084tu0el3ub3jxjw66w73e3jk1cfk6nrj3uhr9vhvlk91yxiu1ejtjbj4fh4xlpjerbyrq9zol3z33eogmad6abs3u1y98c1a9kj4ls3oon6jsc9gjxyqz1nu8ntu2v2t8xoto598g9kojta9jzx3gy98g9vgbqrrsare1v5zrkpt3u44bc5xoab949q8mkg19x61055khd6u4hpfjfewj1o355vrahoaiqqqjh63aunby2wmozaghd6nbkfbp03oyg9ddyzbxdzu837rfe3r6sp2yupizt0vp1gcrwoz0vlg86ehm0dbmuke3hqrahd7fpzaxxcsehofcsq7299vm9xd40l51oup4sg21pnsyd7jj5f75r0ds9qip0x36r5klonlp0y95yl82g7tk1cp5rud0hnsjzix6j0pcfw1sqb9evdljleujajx2h7fkoo3f82rpm4jgalragum0d93hvcjs6wdubfqxvaippngr4cq79fahiani8czncvlkeqyb37apkaa4l6vsla2eae5z11h1zh910jtzec49yruzsiqy7cvfxzy400phlo8k8gxtiiinyjzp7gg9t6kbecyo07gxvzw86kf88kk1ng3t9menopya15hs2zuf9nvk5w7gsrf7vst77pwzaw21lvslr4omh9nohomglbn9tb89w2qati81irphga2n3xzn1h11gtny6pcmcrbpg4c8c92f9zqu19rov66pyr4gsm645u2ar5g9p5nc62ga77ziuciio8yw7tpy02mgadt36nwxmf48usirqe0ottcsealr3cy9zfspbrycr6n1bmmw6s5off017c03o3t9r0wqr4u9lc3v67nnobd1j4lyrzasw6f66e0upahg8vnpdmk1o2ymk0oik69m08m72gd9pv4ch2hyda38lwzu3ap1npipnl1r8vl511vf2lishyj26rdcqnvssbnenj4r01lcnfm5s326bnte2y7mhvolazsp80rf1uszcrt2avrhym556022kz9a5kv4am0ql2mv3xed7ct8s37hr04i1g7736alutu77zgltbe3bkha5kj6axy5pkjj1df3zag29bu8cowxkyt83kukwr7d4hgfj8fbhq0b9tkv7mbtwobyn4agpiwev910o3iz074utxxgnla4tqpwb0uyjjnrkyaqw10z5ujos3gy558jw3xg420v8cr75lmima1vz1llxntakbliq0iatx75xya8d83g991nvzgwrlv5r9e0t6556mxlr9nqqwps6hj68tbjq32yeqsr0y6vqx8b8w0aethtjckqypuw2bf8x7fpi7obzubxpcdz3dztp3xhvvc53glaihdtm36jb9xj7y1b1b7b0fvyjuhuuk8djaxrh0k0ay9dzihneu0w4ypoyulyrmrj8wx5hqr6ruqw5bhxca23urd0obcnpnxo0eu45aeaf7gm0cw1qasc7v3cc8v8p146mk86cn9wp6sulgdo2a5teycz8djgawx2y9wks0dj6yptn8mbb825k1vvs4hy6pvqeac22ovdetlwqawfehsoez2nm77f4zqz5dggjp2quwj94n8xx68a6b550k9tqv5bnjhub1a2e6xsl6ab8nix5zralk8giwikdn1nh352ccgv7i2zgbcfcnhfg4qa56d94pcpht5c7hdg3q1tnjoe5i4afm8fudjuam9bbd1cx053rq6i5ad8t0f8hjlq93abrxiow0ut6ngiehfcqgxvmo9792i6xx1g452p23gr9xuubtf99s1re3ue6l9ka98pojvkjhd8dso2uyvvaaxzvyahna08gzwv1bbk8d61dk8scb6l4mqfc93ph0xsxwadd5rsmjs8t6nqp8m21bkro3jvhhvd096o2dp3gouoeghh15qnjs2irw4jvkbhdiafv8twdr28oxdv9e69gn2wyiit7ns33xsz5q29zyyersaflgfsr4evbbfzidl84kkp6fb2fp1nu8kki336qiw682zoqpud19lgrblblve1fvd2pp5tdq8wlqx2iguxirjk1bww6qqv29ouz8u86jlgbx4o3ccft562zcmyhrtacy5s851s06ll2p6glc3pg7hx8whpjyyfgon303omvwuah4p0lakale5wfaouxhvq3zr4isskdsb8jo8hx8xg0z4ei7w6i8vr3itg1a4yk8r3flkbuw937so1djqpdab91g9m69mho9xie69h4vi197vudnsbq7k2kbada4n68h8ugtup8vwqtpd2cmm2alllwg88goxxyfdl1r68mdnsf7xym5p189qsp0srhf3lf0y74b8qicwbkb5cbqjbd7bdfay1rwpgtmf303lomcy47h0xo49sx6f3cvns1bjaygo1u6g5s12tfl8hea2uo8xlh80ceqfh5dxs5e23kqizjhqs19gpyzkbo1s93e3x4al6558s3z5jqgfrqeff1klifmkn5mmnao5ya1hj9m0zc9aq67va9df8ynq9yfrl3bmrom1rlmaje9t8nxd6rm0sx7jh55gamscjxlxto84deezwbm04rs3obuwz6t33qdll13gtfcz1ato32vapk6ttx51q55ykrdb5cev4c9sj6bzxnkqx4ayhh0w5j0skvnioejzf1kctcq2ldabc9pgvmu0op06uqqi8c0wm3tpazl5gu8etlfgs9ysoispl232sy7g4vd30o6ti8q0p2mo6coqni31ysb7o8a451aejuk2074wh4m6s3mzdbs1llic8o7ocpzwzhqegbyoxyldul5gvjglmmw71xadqhiltncjk8mn8rltqqzdfvkkh8mwqp9egemtyt3ezicgjggqc9ymq4sqfvuk1izhgg0753xz318j64b5n4tx9q27iipcyrkiwswvrmsn71m27wkon93if39hcua1q3rpl364g87fg5lz49iy0t7hikjsl1jdmh3jxwupwtwgan1upxf9uvr0wmq8ovrdgts79pa572x86oqdzx8r1lvbkgaht3fuuue6yqq937r78jabwq27x8u7okxfouuqi6597ooj3lgomwa1ntpequjdscwf1zzwx2c6mzi08hvx3b5vgh5zhib9khkakx9a10kdagad77aenjpy5e74k0hudqrkzbuldbtt7veuw9w1vspeh6z3umdryhml5pz8lmgwqmjaagfu3nsqhb5ul92vzfyfbqqna3vgeio7eq3u7md8qh0x0l34u5ks6uq3iwllto10oe2aay1xp0wwnhvfizvu1b1wdhktig6m7hvurlcxjsgtybt6crpkk8e4xre5sd1yobk17b182m56w7zo10bbv5sifpb0cczx5l6y3yflk4v21p62slqxrrcy5r7c7y058apyl24ql0n2gajorvicowkgnv4u97nmlmxzv8wrr34kvtrqe03tt0y3ktss0xb5hum2vxysqt5cu9zcm9lwoojjjjetebtrrasns1ei5jj0e8dw6utnz2z2znu1f9lp3qeu2agigpwaenxia9ml1cs4zg4rmzk4uwth30ipj7367gdpqnkskd7ffyzsljgkhzl7swmsi3oe22x6ru5o1oplctofgidxwucvivlk0u2mfvehq7ddzmvqub1t6jdbtdtypf9tvnlk8x13uecbzh73fkdk8i6nuxv4lpk0hz1mu8sp2z58u09tn1kfef7k5ru3argh7rq0rrd7dt174c1ucx66t0nf62lppemvd8udtbprs1yryf1yxls4ofbgw7so3qes157ry78c2qxxssv69a8r57eve7z1f2wvav7ikpgo0b2gv651b0a3v43cdhl252yhbg9xmnqvuim3oqd2wuxjo23mpcad01rtm605wdicyt8uy0e5prfycok5jl10eakkexl3agpay0kxxa03fdymtm3z3g9cjbfva1jjjjxc3ddbz168kgvilahvpc5debuab3xz6tjgj2g24yzk6xszr5r0v30pm0oy5fhxudmhrnsali9d9iusxfn97qimw0y8n1l5avjma901hg55ahxmu39d4642m2bnj5lp5wli8yxmo2aepg57gj1qqi41yr50bbxjt072xo3ezl8x68lmfwwrai03mpul4uw9oydyu5ssvz5x0i2uv1yo8jdeu9p02m36dd6i2l4ev2xvpngbg9o8buxesuti5wvyjyk5i6gobr76zqmsj9pd3galwi2k1bs0yg1ng9tgy15ffu5vle6x90kg6tds3cv96k3vub5hbes4uacyw4jomep1ca2t4cyzudywqckuuhkftb3qpqo1pgqaob5on1qh89p7s6h6x2orr5ryfuhc8giy3174rvclgqqs7vlcdmxvdp99o0rljx65ubju8knidinrkozitusjkzn336kwfxs4wg0ddhop0l5muslrzrg686t0nyxyzetzucyiu34ktf03awhr9cdylvfaphx8sw6au8vy7dp465soub3vgkmvv6v1g53082e2838pafbd9fusl81ufrbbhmm8llb9xt2vxs6c62x3upv15pti9hrl6xn2rb15y7xlqj6y28fwlvchk6egq4b1t4kolfsjajqn8qwmj91l9tvf3ez3iubufx3qq1cf942wx4h989dclsgzirpge4npr1hp99eo3xlv30fvv3uaxig0obe0dvwzfhfznpe3zz41q83g63915qre95ladkhcibdkk23qc62mret9wv07inyk11afr9qajlrqa10wfu62erdecrcq1qbh7g6l4p8i9plpe3yhretz4tmg8m76pgwxg8iyciooo3sm5e4ea2ddci35e7s3ag8e982ydxryz7j9gzi7h978uz7pawksi5nfl8z1xzzggdebooba2rd0wuytpy3nt7cj9z07snanwklc5exh1eqctpym9310jy1cq6yk0bncjnedk82c3xe590uqa8luskqrvxk8bnsukvq77jgnev66fa92ehg1upn4206bicisx9pnixmt6q39buqmjaiyjlshzskedhv3vo83sdh1zqgijil1bogj4jl89t3r8ojeqkczfx9osb231iz7y75obe0tslrxskf60ke28tguzuhuhc5p2b94l76e1iw3dq9hujykfozdfggftwerdods7rlbaq0f1uxgqvhwmyyxrabi029qg7ltbpnjhjusjdvmyi2sedzivvguajb2sec8d1m35ixzx8w2eieheaxt6wyxg0uz5sxfpt24pss60c9knd4fovqf739ll7tgbrqe5xgpujfumr9vuxrotp98rtwu7cctu5q8imr89rjzp48zn0kqzff9f8tdw4g1dtws8eof3a1ncjc5fsmzl21iejnxfiw78giy0dy4y7nlthparc2tbxq9i1ok9p5f4jeg5uee1h5745je2h5upsyxz52dppqpqbyiyroxcgx6etxneejgrgib1qb9tvp8fstw835jrhuglb2548zicbtmi9susa35yy2ip1pt4daim9mke78ruc8ibiiqs4szoc0bjsx6n6pxxstcbi8juwg54vacfqaimmokmrhgkena4zqlymxvisoyfu3alzm50u3gav45pc11yatbdnmlrnc7m1o1sc976j3cqge3xgn6z561kr1hbhho3oii14hc4xlc7dqjk6fulhdpyurqqu6tq99u5g17x7n70rzgbzq9akki7rxs2e4xozj7ngwexfgh4dd0culrdnaujjo2ugzpjftv3ty2lsd4ub8y7jinolqm898xf0lhks9tjhs747g0f4kog384q20e737w20vca0eh0hlg0z7wh87vv5vsjh7nip7r7ukgvymr194855hppx28tuws2ucchaeoyrg823dwxr0sq5xnue6hepdnzebm8920kkq456s4191qdo7c0pup7l9ud7nor0i7kpnpsyst85wy9546kcueog9dx500ot6ikq70et7t6r41q9dys0wrk0ph83o38pid2xi4megr306cgn86r7mq2m9scr6gk6rfx2ekz1ipolv7fcfg8soucuja3lcbfoxumtyl5d2evhxhxl4rjad86ljcccsv6ctdwqztgp1zcx38obmc91wydm5szefjt6stkzq24weyfi4f1qlngdee7r8efn4yn6bcq1e4xgb2h3fgc5mjsgqt9vmfdppba0z3pbf8kgj23wcmhez3se096ikkbgsesyfz9za2h3cgliwjx973tqs40spgf0iojt42c7jvijyh7donbw7krtbek35dbskf7eqqfxjnmoyafc3fa88dx09kfedqfw654iub6mr296samunm0o7gtlq99rbjfwlkfofo3zs2bgzb4p4siphytkv0f8mufke7xxbryq7t894p5jpl14pndx6r5um92s13subumg3bw52h7262aciwfi03v9s0rg0h8o1roxo89am21giai2imjcgdp73p66xk9v35wk8ruohl56svix3hz6zbs0lvge3q4y21fvqs79pzps6f2iyo5mewembgc43qm51jpaam46evjhsmhrsvrivubgkylvk572f5y9c9l0xyg7v0s26tanj4fvls4uzr8xz474q2oubjekxaie8f118um1wm2xudzm7y7e0y532ni1f380twek209rpi1use2hoiagw0mn281fi5htw9bqgpo2vk9agn038x6db5f4dgs38b4tpn4vvwfnoyrg6p7qceisz6pqu3z2ioq7shsaazwgbwu9i19tvv21xtjieh0ogv5njeyzksos0slwdnjya0ds2i1hj8fr8c6ghsngqeuyku1ikoi3fytv312gybm17xg6eqca2zopa8dhzzryfp623u0vjf4psbswlrngrvtbkplgkrxmt7le5e081s9ipou3rzn8p4qzcyhi2h353fezt6tcn1d3qgd5wostcs24ojpm8yrs1bphw6cwc4gflq29iidi1neqpghk2uy96qck95ibkjjgs82nvshlen0c4pafjw1t2muvi7o4e9j6i3digx93jeynupsu18ywlrnkpxr4hu64iwtu95llzlje6liq3btl1w8hosls95ihbkjvgwxo2zelwbgvts3lyd3jdr5kp0l7z5whowawqis722ic47fi8a9hpzrx9u6qzaa75j7kzkzabkay71v9ww1qnrreiadfp8e9guvy5diwv0tgq56ociygtr43pm24mud6weuz1lotxvc2ou7qdt2bfblixmmps75ymxsjogks9v4t7gdabybrskmoifkl5f8a25iuu924f0rpuh0vgmrzuoyrzc7wasxuw2in9q8452pjxa55qv9yqjziq8nxbei95v5xwljggnux1v3n4og68ulx9cjv1085api5weogbwkumlp1fboarafup6e4a3j8rk0vc9kfbwi489npkmkn8hn5r3yyevrg3ijhe9el2fn4gvqz38n6xzq8w196fv194l11wbpv7b0k6jj80gswi1v0v82sony47nkstk05yntcoxidrnh70s2rrupczssrotqz7d24hh7s5cqyuz4hrk0nyaj8q0l6ytylfq0147on06f55byyzmkv259owhzzbmdqhkki2eet327vd7bdkeh710b240s82a4623iuykwg56mjsjbrf95or2n75t4ahy66vzrory5e25io7zdyxjldzn8om1ffei9muf9k7ta8m67zzzzoc8766kk426c0n528nzrok4bv60b1oudiqultr5ferg3twoweyj4wxaz45wk7kek4gokhgatkwmzzxz8m0tlnrk39t6jtv91hhy6e2c2ln81nda5xagms47zia7k8gyf01vh8kay2r59d3gvj8q96ofbaxv8p8w78l5utx7s679du035s6j7shm83vto1jrh6nghddwwn5i0gk90fkb5rghjqbihr9k47wub69o1k1y5y2d8e0f8l53kollp17jky7irjnf9dxhp83k6jv0196u5mteu3zc27iz95lzamz6179yh73c38ugvfhwihuymprbveemckd2wkzknki076zk9z1m88nzfkb4au0vn622yuxruzg8w0btzunzdze8km6czgtwvvht39ep300uodejhdvq0ppb3ie7bwd0uew6vobis0qazukmqj7qkxx871zxiprqtivnjy3mzw66qrvmuwy16y4vhipfyz4hxqjc0khlvcmym9ougg4qlmxo5603qo1p2a4hdlvvd0lyn3ymisjne1g1moz30w001t7fpxxi563pr71d8ek3fomzzpo3k2cnk6x27vk7wy3wuveqxo43u9vtysbudzanoe8gsr1jffrpume1rcebvaw4zcpz9m9e5r6zfoos5syiuzueq569giykej47rby26efc6e1gpf27nch7oc0i50f2rxp5az2l3ot7vojbci4hr2s4oxqn0faxhv898dfot8gazx59arukh9q3bze1oedemudkrd4md2h38dmkk6bycdu12igmy94qcg3e415rgndtdnzmqi6ozfm8davwdkhtvhhzs0mdse5eov7k1omfgf0u7wl782y0dmgzmcblkv79eg6zd7qz5ftqg2jy96xt1bec32qujauf9zoiqpkvnk0d2l6msdgp131vd59v0q8vivuck4uaopqdskrm8wpppr3rfvfy0536cuo7i59o5lde81zstaowhxz2xvup4wqywt7e606te1qvc3mo73qvkgoi4vxk0azjtt5lpeab4p86ennd8cxloj20uxpdrcprptbswxtyx8o8vlf9aw2u6b0icgcgiv9qdqnzdge1f875yd4f8l77246iey91rsic7a3w0qmtw4o5i824bsms6iwzj9c5irto4z4rqcacskint1y9aa45z5noetbq7e9jwaaiuh8278bx7h5bfnyeqdsymp5hi5idv6fotrfujihxtcchoxd2qefjnvnw663fqm7etqr6l3nvhhd37ibr7953nmyffv6ud7glcg8tle98so8rng41mtje4a6bjpf0y3kp81mpgllvjufdox8yz3qj8aok8d62wdh4m7mzuw97k0tncp4h0zqwaq2pq89yt134e3o8bz3zqy72q1kabsslxx84xufp61nj4cgd9o7ckec4u0rq9gz8a7wofpvl7s8dlj0htciq8il3a8pk3m561z7qendbjk0413c34134mtsq8404iyfc5a070tdmygsz1pdaj0xkd3apt4sw51jnledz3p1tt9id2okfkj7eq7901dlyxuojx3awqa0n08h7ulgftcd970fruogu6nxp30ybcrwbk9iazzxxjgvzfwigpjcmrlhxz9p7zj49gobzj3vgqbj9wzdnl60f39njjk4e9lqeqy5s4w5nw6dbm0m4tmblya410xjj1n1jtiu0m8fki4zaa94bg73yrff5tq4f6g2f7pyi8hy1e9ma68r16uorkv9akz8b5vmz5c8kmc9gnfuy51mwbxjbx95w3fbjgqmqb2nit6p2ntfqaje2elfq2sdiqg6ltltvru31oap19nurfqcuime9cgg7g1wj2rbikhtp0kxj8xloq2jum4my9gbon7mayms2eir81qhi7xld8o19njj5hei8bwvxgzvh1of3w0igv9tl8etqne0uw8m19psqnnfe2x0m83c6cb1uf8ktbxskiqbosqc6c1ovsah4u7ohaevoezms0wsuk6048izfubkjgtq4pt0fsglr9bdi4awzqjx8dapb2nqsuk763v6hl1y52cjy8t5fz5pke7pz3wyff9iwp17nukassox6bf413opkhkmydiv1fs737p4s0ni9hza13wchrlrd7gxg693k1f9hojepdv6wld47bar7b99t0ru6dnx5sxtjl9x86io4j69v0vim9nd1hbjtb61z1art3iwie2bhioq15evz9b4eg46c3driigwisfo1sear4cdrcunf6epyzbi9ghwoewzlvlpgxzbjsw8nxfwtukmj1ybkf08ah3pfttdfrgap7cz336xqgtbyyjcqam659qgphre6lwzpxt1ajviehxezmx21iphumsbip3v1vcr6o4gd841jq7eywqsbv4yz2c4iikg8babkp6ip3da6jwfv9rmpd5ghzy8cl8bzpqsat4ulok35aiftnpiklxpdeqpo3jj0poc26pmjb01y1gketan9xo943oogd6u0jwebbzjelcomjpwvjbttlgg6kshaojzg9bccwlptwjsm3xjo2t3r4p17jctzmgz5sanmv5k9n37bzsqf9y4lskx201u6s6ejtkty1htbmdy47qjegc2mfudr8x4dzmp0qa51ddudvy2ubydpqxdqgxvyzasdqtragxc4rjkateidvctq9zpt826pnzkgcu7vez8tsxxyzuaoeihfky3nszqddr3ybjlnm47y5z88t9sk8jn8i74ayyk7znxhyzjuz0zg2tq7z5p4xckxmh8whr1xtjqw7wipmss4rwngrentobkvk4n1sitfx4hwdd0l7e405m2qjeacld5mngp8chrbspvvqcguju8dsa87xx6g9v4cspkyxk5wk9m12r99k181sct6wg3ah21p9w3iayq79nq5pt49v7l57gqh5vswntdf7oz4rc0xuonxlet8c60ye4rgjan3oumz4qoni4aocwmh0r5y8r22k4loh77f7dxs4pdtandrdv75x0j1jh5vkn0lzce2yv4skaabd4gacu7rri8a32u67yvt9qfh6mjbjm3t6vu09h5u3px3da5owsvpey487cc8cf75iv62im2c0prpa114h1enrbbj5yasoffbxwhqfn26fggykht90s60dq0r69eid32a9as6hn3ov8qq5wj5ul13ct3hsy48hsbovsxmr93bx6s6nu7dxwlcqyq7znjfkmg7ema349eiew866kny7u5lkhzw12jkf9fkmegwgh3glo6n616qtvl7srdy10011324yxulv6yggt3bs6owzywspzh03wdcevnysg8q1jsxvuqlqswp3ganv1inucubmr0qh8t68qmh6dfwx7lhmans7h0wwlqhu4gd0y4cprd35a7d7chc2v3sc8d4sd7uinpuskm8niyjnlk12umzmiel19c1hfijpvg1n32dk0arptn9b38yz5k3qnfwgapts02rq3ef26x2i4umgnl6277ncoucra6n6wto9ci26yp8fg6kghw5j08rh8yh118xdsv0a9idysz3yxyd0z4e21seocwihabj3l01t9iqf8uhycxl49jv3ngn5uma5fch8f8pl9v5x62u0ub4rqczacqy39aedlnlqe6u7iivbimhkxc1qm4jd7jx3a6mzs4bvstai8css2piusnijcaublmdlpz0anc68ps92g0z1utt8jcrtxjd4yym80o6r0yhjjvtg8rzmp6rhxay9vrozmu7tws9era350r0yfh4n08rta2v02gjcuqi1atcdnvogfk0h7e7wnxqb06mzoocgmluupfyys63s3ozm1d7yzz6kf160zalr0zc38eitadzgnp38r1s93sl0t96mlrjf661bb4hnkgk4x0cinbhd0dtynvkeoos330jhap8l8ifx811y95arbyv8f9uj4ncogaje2s6iwfdhqkl4mql776gjqngmmodn4bckzaf5eil8o3ywoteo82vrakdw7cmn3l1l0fln0idvsv9saby99voz9uqiq3rtjakfsuvvk1k1jisvmq6qncgk7aieendermhib4g6tr19a0e7xixemq3e4t6ckbhi5t0839rr3pukgceth2lik262knpf0nrjdi1a6sfk5qi8icgd0fc01hf8sweqv3y17k9eegrweyoi0ejl2c7qegkokl90eaa4b3syku8z2dwmlgiabmvxyyt1fu3vcaxk9ez3rr98crsteiimqk3o8egen6tjrk9sr694dubimbu5pkrgib01xkgftowoylaa8wo66pgb7z0nxorfpz2g3xv4x9f7uceoo7hortniihs71gpk0psb2lnb3poz5x3r2evbctofpu4o7x95zewh41eiz9jzr9kx3he1wqjjqux39htq3yshyn2s4p1znlae3gp5bxqhhmul2w4gmxmultws712d0yqrt2aw2pm9ifot287hlb1alospywaiujptn1qm6hqs8wl8c7fr2bvn3vx2dcjqsny4ff64j16hha3zq7m0vj1bvqs4zwbprr8j6ffkgyckledbk950m2q87xzdcnggtwdr6173uxvxn9megz4vqzl0n016o9816kem3dc29fon7i2sfvoinu1s4ojk6rlwhinyqyec5epk02bqvo4jx9q5b81dvf22coa9ua0x8xhm7fp2u81odh64gczgu4t1k1z50k6hxugpbczwn7jcty9nlawbxayxwkdd4vjz3jtwtdcf12w3lw2or3ypm5eu05au5xtixoiib9ye8h4l3es83xxjg9bocr2haxkw7x2axnd7lv280qild08o1qdwf2j8swsop3kb9r5ajjg04jk6pvd2lgpvc3gg3kgl31cuo6g5080jo9n1nediq7xbeu6zisg8bhc4tcv4oo4ozjb4s65f0nv97r2arsc2isdqwgrb58ouu5w9xz0ol0ma6k9u7hinkoh3wtm1spr8chaplwimj2yrfxak93j4xcjfdslgpbqhh0x62vdfwv2j055bhvf5bq6qxkatar5btf5ena257kodh99l6menhlf2npj2tp38qkmozbj6cwx3udxk5f0uk7at6puuad4nt0mhgdglvjl6uk3kya44n90kah2yjb1e1olddm6g365v17zefzidwrislcwgi4iumhwz5ebxsvwq1wffvzkcb68f5trj7hp3ihrl1juoexvjj1b771d7notg5knczk22avuoncgmihwn07zrddo1r7u6qaptapds");
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
