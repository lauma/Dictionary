/**
 * Gramatiku apstrādes loģika - klases, kas vispārīgi apraksta, kādi mēdz būt
 * gramatiku apstrādes likumi.
 *
 * Vispārīgi runājot, katrs likums raksturo, ko darīt, ja gramatikas teksts
 * sākas ar kādu noteiktu teksta virkni. Likums var saturēt arī specifiskākus
 * šķirkļavārda ierobežojumus, kas vai nu pārbauda, ka nav kļūdas, vai arī
 * tālāk precizē piešķiramo paradigmu un/vai karodziņus.
 *
 * Izveidots 2015-10-26.
 * @author Lauma
 */

// TODO: pārskatīt, vai likumos iekšā ar karodziņu komplektiem nevajag operēt kā ar Flags nevis Set
package lv.ailab.dict.tezaurs.analyzer.gramlogic;