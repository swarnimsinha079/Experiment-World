package com.example.data.repository

import com.example.data.model.*

object LabRepository {

    val periodicElements = listOf(
        Reagent(
            id = "H",
            name = "Hydrogen",
            formula = "H",
            category = ReagentCategory.NON_METAL,
            state = "Gas",
            colorHex = "#93C5FD",
            atomicWeight = 1.008,
            description = "Lightest element in universe, diatomic gas H2 with flammable combustibility.",
            hazardNote = "Highly flammable gas; forms explosive mixture with oxygen.",
            atomicNumber = 1, group = 1, period = 1, electronConfig = "1s¹"
        ),
        Reagent(
            id = "He",
            name = "Helium",
            formula = "He",
            category = ReagentCategory.NOBLE_GAS,
            state = "Gas",
            colorHex = "#DDD6FE",
            atomicWeight = 4.003,
            description = "Colorless, odorless, chemically inert noble gas with complete 1s² shell.",
            hazardNote = "Non-reactive asphyxiant in high concentrations.",
            atomicNumber = 2, group = 18, period = 1, electronConfig = "1s²"
        ),
        Reagent(
            id = "Li",
            name = "Lithium",
            formula = "Li",
            category = ReagentCategory.ALKALI_METAL,
            state = "Solid",
            colorHex = "#FCA5A5",
            atomicWeight = 6.94,
            description = "Soft, silvery-white alkali metal with highest specific heat capacity of solids.",
            hazardNote = "Reacts with water forming lithium hydroxide and hydrogen.",
            atomicNumber = 3, group = 1, period = 2, electronConfig = "[He] 2s¹"
        ),
        Reagent(
            id = "C",
            name = "Carbon",
            formula = "C",
            category = ReagentCategory.NON_METAL,
            state = "Solid",
            colorHex = "#475569",
            atomicWeight = 12.011,
            description = "Tetravalent foundation of organic chemistry; allotropes include graphite and diamond.",
            hazardNote = "Combustible powder.",
            atomicNumber = 6, group = 14, period = 2, electronConfig = "[He] 2s² 2p²"
        ),
        Reagent(
            id = "N",
            name = "Nitrogen",
            formula = "N",
            category = ReagentCategory.NON_METAL,
            state = "Gas",
            colorHex = "#A5B4FC",
            atomicWeight = 14.007,
            description = "Diatomic N2 gas composing 78% of Earth's atmosphere, bound by triple covalent bond.",
            hazardNote = "Inert at room temperature; compressed gas.",
            atomicNumber = 7, group = 15, period = 2, electronConfig = "[He] 2s² 2p³"
        ),
        Reagent(
            id = "O",
            name = "Oxygen",
            formula = "O",
            category = ReagentCategory.NON_METAL,
            state = "Gas",
            colorHex = "#67E8F9",
            atomicWeight = 15.999,
            description = "Highly reactive non-metallic oxidizing agent essential for cellular respiration.",
            hazardNote = "Strong oxidizer; accelerates combustion intensely.",
            atomicNumber = 8, group = 16, period = 2, electronConfig = "[He] 2s² 2p⁴"
        ),
        Reagent(
            id = "F",
            name = "Fluorine",
            formula = "F",
            category = ReagentCategory.HALOGEN,
            state = "Gas",
            colorHex = "#FDE047",
            atomicWeight = 18.998,
            description = "Most electronegative element on the periodic table (Pauling 3.98). Extremely aggressive oxidizer.",
            hazardNote = "Extremely toxic and corrosive halogen gas.",
            atomicNumber = 9, group = 17, period = 2, electronConfig = "[He] 2s² 2p⁵"
        ),
        Reagent(
            id = "Na",
            name = "Sodium",
            formula = "Na",
            category = ReagentCategory.ALKALI_METAL,
            state = "Solid",
            colorHex = "#E2E8F0",
            atomicWeight = 22.99,
            description = "Soft, low-density alkali metal with 1 valence electron in 3s orbital. Explosive with water, incandescent with chlorine.",
            hazardNote = "Dangerous when wet! Exothermic explosion hazard.",
            atomicNumber = 11, group = 1, period = 3, electronConfig = "[Ne] 3s¹"
        ),
        Reagent(
            id = "Mg",
            name = "Magnesium",
            formula = "Mg",
            category = ReagentCategory.ALKALINE_EARTH,
            state = "Solid",
            colorHex = "#CBD5E1",
            atomicWeight = 24.305,
            description = "Lightweight silvery-white metal that burns with a brilliant white incandescent UV flash.",
            hazardNote = "Flammable solid metal ribbon.",
            atomicNumber = 12, group = 2, period = 3, electronConfig = "[Ne] 3s²"
        ),
        Reagent(
            id = "Al",
            name = "Aluminum",
            formula = "Al",
            category = ReagentCategory.TRANSITION_METAL,
            state = "Solid",
            colorHex = "#E0E7FF",
            atomicWeight = 26.982,
            description = "Low-density amphoteric post-transition metal with protective passivating oxide film.",
            hazardNote = "Fine dust poses dust explosion hazard.",
            atomicNumber = 13, group = 13, period = 3, electronConfig = "[Ne] 3s² 3p¹"
        ),
        Reagent(
            id = "P",
            name = "Phosphorus",
            formula = "P",
            category = ReagentCategory.NON_METAL,
            state = "Solid",
            colorHex = "#F87171",
            atomicWeight = 30.974,
            description = "Essential macro-nutrient in ATP and DNA. Exists in red, white, and black allotropes.",
            hazardNote = "White phosphorus is spontaneously flammable in air.",
            atomicNumber = 15, group = 15, period = 3, electronConfig = "[Ne] 3s² 3p³"
        ),
        Reagent(
            id = "S",
            name = "Sulfur",
            formula = "S",
            category = ReagentCategory.NON_METAL,
            state = "Solid",
            colorHex = "#FBBF24",
            atomicWeight = 32.06,
            description = "Bright yellow crystalline solid forming crown-shaped octasulfur (S8) molecular rings.",
            hazardNote = "Combustible; emits noxious SO2 gas on burning.",
            atomicNumber = 16, group = 16, period = 3, electronConfig = "[Ne] 3s² 3p⁴"
        ),
        Reagent(
            id = "Cl",
            name = "Chlorine",
            formula = "Cl",
            category = ReagentCategory.HALOGEN,
            state = "Gas",
            colorHex = "#A3E635",
            atomicWeight = 35.45,
            description = "Pungent, suffocating greenish-yellow halogen gas (Cl2). Vigorous electron acceptor forming ionic chlorides.",
            hazardNote = "Toxic and corrosive oxidizer gas.",
            atomicNumber = 17, group = 17, period = 3, electronConfig = "[Ne] 3s² 3p⁵"
        ),
        Reagent(
            id = "K",
            name = "Potassium",
            formula = "K",
            category = ReagentCategory.ALKALI_METAL,
            state = "Solid",
            colorHex = "#C084FC",
            atomicWeight = 39.098,
            description = "Soft alkali metal that tarnishes rapidly; burns with characteristic lilac flame.",
            hazardNote = "Reacts violently with moisture, catching fire immediately.",
            atomicNumber = 19, group = 1, period = 4, electronConfig = "[Ar] 4s¹"
        ),
        Reagent(
            id = "Ca",
            name = "Calcium",
            formula = "Ca",
            category = ReagentCategory.ALKALINE_EARTH,
            state = "Solid",
            colorHex = "#E2E8F0",
            atomicWeight = 40.078,
            description = "Reactive alkaline earth metal, essential structural component of bone and cellular signaling.",
            hazardNote = "Reacts steadily with water yielding H2 gas.",
            atomicNumber = 20, group = 2, period = 4, electronConfig = "[Ar] 4s²"
        ),
        Reagent(
            id = "Fe",
            name = "Iron",
            formula = "Fe",
            category = ReagentCategory.TRANSITION_METAL,
            state = "Solid",
            colorHex = "#64748B",
            atomicWeight = 55.845,
            description = "Ferromagnetic transition metal fundamental to steel engineering and hemoglobin oxygen transport.",
            hazardNote = "Flammable fine metal powder.",
            atomicNumber = 26, group = 8, period = 4, electronConfig = "[Ar] 3d⁶ 4s²"
        ),
        Reagent(
            id = "Cu",
            name = "Copper",
            formula = "Cu",
            category = ReagentCategory.TRANSITION_METAL,
            state = "Solid",
            colorHex = "#F97316",
            atomicWeight = 63.546,
            description = "Reddish-orange transition metal with exceptional electrical and thermal conductivity.",
            hazardNote = "Heavy metal toxic to aquatic life.",
            atomicNumber = 29, group = 11, period = 4, electronConfig = "[Ar] 3d¹⁰ 4s¹"
        ),
        Reagent(
            id = "Zn",
            name = "Zinc Metal",
            formula = "Zn",
            category = ReagentCategory.TRANSITION_METAL,
            state = "Solid",
            colorHex = "#94A3B8",
            atomicWeight = 65.38,
            description = "Transition metal with reduction potential E° = -0.76V. Highly reactive with acids releasing hydrogen.",
            hazardNote = "Forms flammable hydrogen gas when reacted with acids.",
            atomicNumber = 30, group = 12, period = 4, electronConfig = "[Ar] 3d¹⁰ 4s²"
        ),
        Reagent(
            id = "Br",
            name = "Bromine",
            formula = "Br",
            category = ReagentCategory.HALOGEN,
            state = "Liquid",
            colorHex = "#B91C1C",
            atomicWeight = 79.904,
            description = "Dense, reddish-brown fuming liquid halogen at standard room temperature.",
            hazardNote = "Severely corrosive to skin; toxic vapors.",
            atomicNumber = 35, group = 17, period = 4, electronConfig = "[Ar] 3d¹⁰ 4s² 4p⁵"
        ),
        Reagent(
            id = "Ag",
            name = "Silver",
            formula = "Ag",
            category = ReagentCategory.TRANSITION_METAL,
            state = "Solid",
            colorHex = "#F1F5F9",
            atomicWeight = 107.87,
            description = "Lustrous precious metal with highest electrical conductivity of any pure element.",
            hazardNote = "Precious metal.",
            atomicNumber = 47, group = 11, period = 5, electronConfig = "[Kr] 4d¹⁰ 5s¹"
        ),
        Reagent(
            id = "I",
            name = "Iodine",
            formula = "I",
            category = ReagentCategory.HALOGEN,
            state = "Solid",
            colorHex = "#581C87",
            atomicWeight = 126.90,
            description = "Dark violet-black crystalline solid that readily sublimes into a vivid purple vapor.",
            hazardNote = "Corrosive and staining oxidizer.",
            atomicNumber = 53, group = 17, period = 5, electronConfig = "[Kr] 4d¹⁰ 5s² 5p⁵"
        )
    )

    val laboratorySolutions = listOf(
        Reagent(
            id = "H2O",
            name = "Water",
            formula = "H₂O",
            category = ReagentCategory.SOLVENT,
            state = "Liquid",
            colorHex = "#38BDF8",
            atomicWeight = 18.02,
            description = "Universal polar solvent with high specific heat capacity and hydrogen bonding.",
            hazardNote = "Neutral solvent (pH 7.0)."
        ),
        Reagent(
            id = "HCl",
            name = "Hydrochloric Acid",
            formula = "HCl",
            category = ReagentCategory.ACID,
            state = "Aqueous",
            colorHex = "#F87171",
            atomicWeight = 36.46,
            description = "Strong monoprotic mineral acid that dissociates completely into H⁺ and Cl⁻.",
            hazardNote = "Corrosive. Fuming acid with strong proton donation."
        ),
        Reagent(
            id = "NaOH",
            name = "Sodium Hydroxide",
            formula = "NaOH",
            category = ReagentCategory.BASE,
            state = "Aqueous",
            colorHex = "#818CF8",
            atomicWeight = 40.00,
            description = "Strong caustic base that provides OH⁻ hydroxide ions. High solubility.",
            hazardNote = "Caustic. Can cause chemical burns to organic tissue."
        ),
        Reagent(
            id = "CuSO4",
            name = "Copper(II) Sulfate",
            formula = "CuSO₄",
            category = ReagentCategory.SALT,
            state = "Aqueous",
            colorHex = "#2563EB",
            atomicWeight = 159.61,
            description = "Vibrant blue cupric salt solution containing hydrated [Cu(H2O)6]²⁺ complex.",
            hazardNote = "Harmful if swallowed; toxic to aquatic life."
        ),
        Reagent(
            id = "H2O2",
            name = "Hydrogen Peroxide",
            formula = "H₂O₂",
            category = ReagentCategory.OXIDIZER,
            state = "Liquid",
            colorHex = "#BAE6FD",
            atomicWeight = 34.01,
            description = "Strong oxidizer with an unstable single peroxo (O-O) bond.",
            hazardNote = "Vigorously decomposes in the presence of transition metal catalysts."
        ),
        Reagent(
            id = "KMnO4",
            name = "Potassium Permanganate",
            formula = "KMnO₄",
            category = ReagentCategory.OXIDIZER,
            state = "Solid",
            colorHex = "#9333EA",
            atomicWeight = 158.03,
            description = "Intense purple crystalline solid. Potent oxidizing agent and catalyst.",
            hazardNote = "Strong oxidizer. Stains skin and materials dark brown."
        ),
        Reagent(
            id = "CaCO3",
            name = "Calcium Carbonate",
            formula = "CaCO₃",
            category = ReagentCategory.SALT,
            state = "Solid",
            colorHex = "#F1F5F9",
            atomicWeight = 100.09,
            description = "Main component of limestone, marble, and eggshells. Insoluble in neutral water.",
            hazardNote = "Reacts with acid releasing gaseous carbon dioxide."
        ),
        Reagent(
            id = "Phenol",
            name = "Phenolphthalein",
            formula = "C₂₀H₁₄O₄",
            category = ReagentCategory.INDICATOR,
            state = "Liquid",
            colorHex = "#F43F5E",
            atomicWeight = 318.32,
            description = "pH indicator: Colorless in acidic/neutral media (pH < 8.2), vivid fuchsia pink in base (pH > 10).",
            hazardNote = "Laboratory indicator."
        )
    )

    // Combined reagents list available in laboratory
    val reagents: List<Reagent> = (periodicElements + laboratorySolutions).distinctBy { it.id }

    val reactions = listOf(
        // Sodium + Chlorine (Synthesis of Table Salt)
        ChemicalReaction(
            id = "RXN_NA_CL",
            title = "Exothermic Salt Synthesis (Sodium + Chlorine)",
            reactantIds = setOf("Na", "Cl"),
            balancedEquation = "2Na(s) + Cl₂(g) → 2NaCl(s)",
            animationType = ReactionAnimationType.SODIUM_CHLORINE,
            deltaH = "-822 kJ/mol (Blinding Incandescent Exothermic Flare)",
            tempTrajectoryC = Pair(24.5, 318.0),
            phTrajectory = Pair(7.0, 7.0),
            observationNotes = "Blinding incandescent bright yellow-white combustion flare, consumption of greenish-yellow chlorine gas, dense billowing plumes of pure white NaCl halite smoke that crystallize into table salt.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Redox Electron Transfer",
                    subtitle = "Valence Stripping: Na → Na⁺ + e⁻  |  ½Cl₂ + e⁻ → Cl⁻",
                    explanation = "Sodium possesses a single 3s¹ valence electron with a low ionization threshold (495.8 kJ/mol). Chlorine has high electron affinity (-349 kJ/mol) desiring to fill its 3p⁵ subshell. The reaction proceeds via an instantaneous collision-induced electron transfer.",
                    thermodynamicDetail = "Electronegativity differential Δχ = 3.16 - 0.93 = 2.23 (strongly ionic). Cl-Cl bond enthalpy (242 kJ/mol) is rapidly cleaved.",
                    safetyNote = "Extreme thermal and optical hazard. Do not look directly into the incandescent flare without UV safety goggles."
                ),
                ReactionStep(
                    stepNumber = 2,
                    title = "Blinding Incandescent Exothermic Flare",
                    subtitle = "Lattice Enthalpy Release (ΔH = -787 kJ/mol)",
                    explanation = "As gaseous Na⁺ and Cl⁻ counter-ions condense, the electrostatic Coulombic lattice energy discharges an astonishing -787 kJ/mol of crystal energy. The local temperature spikes past 300°C, producing intense photon emissions corresponding to sodium optical D-lines (589 nm).",
                    thermodynamicDetail = "Born-Haber cycle calculation proves total standard formation enthalpy ΔH°f = -411 kJ per mole of NaCl (-822 kJ for 2 moles).",
                    safetyNote = "Generates high thermal shockwave. Reaction vessels must be thermal-shock resistant borosilicate glass."
                ),
                ReactionStep(
                    stepNumber = 3,
                    title = "Face-Centered Cubic (FCC) Crystal Nucleation",
                    subtitle = "Halite Salt Grid Architecture",
                    explanation = "Condensing ions arrange into an interlocking 3D cubic lattice where every Na⁺ is octahedrally coordinated by 6 Cl⁻ anions, and vice versa. Billowing white smoke particles settle as microscopic table salt cubes.",
                    thermodynamicDetail = "Crystallographic coordination number = 6:6. Unit cell parameter a = 564.02 pm.",
                    safetyNote = "Allow reaction vessel to cool completely before opening fume hood containment."
                ),
                ReactionStep(
                    stepNumber = 4,
                    title = "Physiological & Global Industrial Utility",
                    subtitle = "Table Salt (NaCl) in Biology & Industry",
                    explanation = "The resulting product is everyday table salt (NaCl). In biological systems, the Na⁺/K⁺ ATPase pump maintains electrochemical gradients necessary for neuron action potentials and cardiac muscular contraction. Industrially, NaCl is the starting material for chlorine gas, lye, and soda ash production.",
                    thermodynamicDetail = "Dissolves in water into completely dissociated electrolytes with high solvation enthalpy.",
                    safetyNote = "Synthesized laboratory salts must never be consumed due to potential laboratory trace contaminants."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_NA_H2O",
            title = "Alkali Metal Hydrolysis (Sodium + Water)",
            reactantIds = setOf("Na", "H2O"),
            balancedEquation = "2Na(s) + 2H₂O(l) → 2NaOH(aq) + H₂(g) ↑",
            animationType = ReactionAnimationType.SODIUM_WATER,
            deltaH = "-368 kJ/mol (Highly Exothermic)",
            tempTrajectoryC = Pair(24.5, 98.4),
            phTrajectory = Pair(7.0, 13.8),
            observationNotes = "Rapid melting into a shiny molten globule, sizzling across the surface, vivid yellow-orange flame ignition, steam and popping acoustic burst of hydrogen gas.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Valence Electron Transfer",
                    subtitle = "Atomic Ionization: Na → Na⁺ + e⁻",
                    explanation = "Sodium possesses an electronic configuration of [Ne] 3s¹. Its extremely low ionization energy (495.8 kJ/mol) causes it to impulsively surrender its valence electron to the highly polar water dipole.",
                    thermodynamicDetail = "First ionization potential: 5.14 eV. Polar water molecules facilitate rapid dielectric stabilization of the liberated cation.",
                    safetyNote = "Extreme explosion danger. Never handle pure alkali metals with bare hands or expose them to moisture."
                ),
                ReactionStep(
                    stepNumber = 2,
                    title = "Exothermic Enthalpy Release",
                    subtitle = "Hydration Heat & Molten State",
                    explanation = "The hydration enthalpy of newly born Na⁺ cations along with hydroxide bond formation discharges -368 kJ per mole. This immense heat instantly elevates temperature past sodium's melting threshold (97.8°C), shaping it into a floating incandescent bead.",
                    thermodynamicDetail = "Standard enthalpy change ΔH° = -368 kJ/mol. Heat capacities of water absorb the thermal surge.",
                    safetyNote = "Thermal spatters of caustic molten sodium can cause severe deep-tissue chemical burns."
                ),
                ReactionStep(
                    stepNumber = 3,
                    title = "Hydrogen Gas Evolution & Ignition",
                    subtitle = "Effervescence & Acoustic Pop",
                    explanation = "Liberated electrons reduce protons into hydrogen free radicals which dimerize into molecular H₂ gas. As local temperatures exceed the hydrogen auto-ignition point (500°C), atmospheric oxygen ignites the gas with an orange-yellow sodium D-line optical emission.",
                    thermodynamicDetail = "Combustion of hydrogen: 2H₂(g) + O₂(g) → 2H₂O(g) (ΔH = -484 kJ). Produces characteristic percussive sound shock.",
                    safetyNote = "Always carry out under a protective fume hood with blast shield."
                ),
                ReactionStep(
                    stepNumber = 4,
                    title = "Caustic Hydroxide Solution",
                    subtitle = "Strong Alkaline Shift (pH 13.8)",
                    explanation = "The resulting aqueous medium is now saturated with free OH⁻ ions, converting clear water into a caustic Sodium Hydroxide (NaOH) lye solution. In industry, this reaction is controlled via membrane cell electrolysis to manufacture bleach, soaps, and pharmaceuticals.",
                    thermodynamicDetail = "pOH = -log[OH⁻] ≈ 0.2, yielding an exceptionally elevated pH of ~13.8.",
                    safetyNote = "Neutralize spills using mild buffered weak acid (e.g. dilute acetic or citric acid) before disposal."
                )
            )
        ),
        // Magnesium + Oxygen Combustion
        ChemicalReaction(
            id = "RXN_MG_O2",
            title = "Magnesium Ribbon Combustion",
            reactantIds = setOf("Mg", "O"),
            balancedEquation = "2Mg(s) + O₂(g) → 2MgO(s)",
            animationType = ReactionAnimationType.MAGNESIUM_OXYGEN,
            deltaH = "-1204 kJ/mol (Blinding White UV Flash)",
            tempTrajectoryC = Pair(24.5, 380.0),
            phTrajectory = Pair(7.0, 10.5),
            observationNotes = "Blinding, brilliant pure white light flare emitting high-intensity UV radiation, leaving a crumbly white ash residue of magnesium oxide.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Thermal Activation & Oxide Ignition",
                    subtitle = "Mg(s) → Mg²⁺ + 2e⁻",
                    explanation = "Once heated past its ignition point (473°C), magnesium readily reacts with molecular oxygen.",
                    thermodynamicDetail = "Enormous standard enthalpy of formation: -601.7 kJ/mol MgO.",
                    safetyNote = "Produces intense UV radiation that can damage retinal eyesight. Never view directly without shade 5+ filters."
                ),
                ReactionStep(
                    stepNumber = 2,
                    title = "Solid Magnesium Oxide Ash",
                    subtitle = "Basic Oxide Synthesis",
                    explanation = "Fine white powder of MgO forms. When introduced to moisture, it slightly hydrolyzes to basic Mg(OH)2 (Milk of Magnesia).",
                    thermodynamicDetail = "Refractory ceramic melting temperature of MgO exceeds 2852°C.",
                    safetyNote = "MgO ash is a harmless basic oxide once cooled."
                )
            )
        ),
        // Hydrogen + Oxygen Combustion
        ChemicalReaction(
            id = "RXN_H2_O2",
            title = "Hydrogen Oxy-Combustion",
            reactantIds = setOf("H", "O"),
            balancedEquation = "2H₂(g) + O₂(g) → 2H₂O(g) ↑",
            animationType = ReactionAnimationType.SODIUM_WATER,
            deltaH = "-484 kJ/mol (Rocket Propellant Combustion)",
            tempTrajectoryC = Pair(24.0, 240.0),
            phTrajectory = Pair(7.0, 7.0),
            observationNotes = "Rapid explosive acoustic detonation, pale blue high-temperature flame, condensed water droplets forming on cool walls.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Radical Chain Propagation",
                    subtitle = "H• + O₂ → •OH + O•",
                    explanation = "Branching radical chain reaction propagates with supersonic speed through the gas mixture.",
                    thermodynamicDetail = "Exothermic explosion threshold driven by exponential radical multiplication.",
                    safetyNote = "Severe explosive blast risk in closed vessels."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_HCL_NAOH",
            title = "Acid-Base Neutralization",
            reactantIds = setOf("HCl", "NaOH"),
            balancedEquation = "HCl(aq) + NaOH(aq) → NaCl(aq) + H₂O(l)",
            animationType = ReactionAnimationType.NEUTRALIZATION,
            deltaH = "-57.3 kJ/mol (Enthalpy of Neutralization)",
            tempTrajectoryC = Pair(24.0, 37.5),
            phTrajectory = Pair(1.0, 7.0),
            observationNotes = "Mild thermal warming, solution remains clear or shifts indicator color to neutral green. Formation of saline solution.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Hydronium & Hydroxide Collisions",
                    subtitle = "Grotthuss Proton Hopping Mechanism",
                    explanation = "Strong acid HCl yields H₃O⁺ while strong base NaOH yields OH⁻. Protons rapidly migrate through water's hydrogen bond network via the Grotthuss hopping mechanism.",
                    thermodynamicDetail = "Proton mobility in water is over 4x faster than other simple cations.",
                    safetyNote = "Wear safety goggles; concentrated acids and bases require careful titration."
                ),
                ReactionStep(
                    stepNumber = 2,
                    title = "Water Molecule Synthesis",
                    subtitle = "Equilibrium Constant Kw = 1.0 × 10⁻¹⁴",
                    explanation = "Hydronium and hydroxide combine: H⁺(aq) + OH⁻(aq) → H₂O(l). The net ionic reaction is universally identical for all strong acid-strong base pairings.",
                    thermodynamicDetail = "ΔH°neut = -57.3 kJ/mol universally for all strong monoprotic acid-base neutralizations.",
                    safetyNote = "Gradual exothermic warming occurs in the beaker."
                ),
                ReactionStep(
                    stepNumber = 3,
                    title = "Spectator Ions & Equilibrium",
                    subtitle = "Isotonic Saline Formulation",
                    explanation = "Sodium (Na⁺) and chloride (Cl⁻) ions remain completely decoupled spectator ions. Upon boiling off the solvent, crystalline cubic halite (NaCl) precipitates out.",
                    thermodynamicDetail = "Gibbs Free Energy ΔG° = -79.9 kJ/mol, indicating a strongly spontaneous reaction.",
                    safetyNote = "Solution is safe and non-hazardous once stoichiometric equivalence is reached (pH 7.0)."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_H2O2_KMNO4",
            title = "Elephant's Foam Catalytic Breakdown",
            reactantIds = setOf("H2O2", "KMnO4"),
            balancedEquation = "2H₂O₂(aq) → 2H₂O(l) + O₂(g) ↑ [KMnO₄ Catalyst]",
            animationType = ReactionAnimationType.ELEPHANT_TOOTHPASTE,
            deltaH = "-98.2 kJ/mol (Catalytic Decomposition)",
            tempTrajectoryC = Pair(23.0, 84.5),
            phTrajectory = Pair(6.8, 7.2),
            observationNotes = "Instant eruptive bubbling, massive column of warm steaming oxygen gas and foam rising rapidly from the vessel.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Catalytic Surface Lowering",
                    subtitle = "Activation Energy Drop",
                    explanation = "Permanganate ions provide an alternate reaction pathway with substantially lowered activation energy for cleavage of the peroxide O-O bond.",
                    thermodynamicDetail = "Lowers activation energy from ~75 kJ/mol to less than 48 kJ/mol.",
                    safetyNote = "Generates high thermal steam and hot liquid projection."
                ),
                ReactionStep(
                    stepNumber = 2,
                    title = "Oxygen Gas Eruption",
                    subtitle = "Disproportionation Redox",
                    explanation = "Oxygen is oxidized from -1 oxidation state in peroxide to 0 in molecular O₂. The surging gas traps liquid and forms a dense bubbling column.",
                    thermodynamicDetail = "O₂ gas expands exponentially according to the Ideal Gas Law PV = nRT.",
                    safetyNote = "Hot oxygen supports combustion; keep away from open flames."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_ZN_HCL",
            title = "Zinc Metal Displacement Effervescence",
            reactantIds = setOf("Zn", "HCl"),
            balancedEquation = "Zn(s) + 2HCl(aq) → ZnCl₂(aq) + H₂(g) ↑",
            animationType = ReactionAnimationType.EFFERVESCENCE,
            deltaH = "-153 kJ/mol (Single Displacement)",
            tempTrajectoryC = Pair(24.0, 52.0),
            phTrajectory = Pair(1.0, 3.8),
            observationNotes = "Steady vigorous stream of hydrogen microbubbles nucleating on the metallic zinc surface, gradual dissolution of metal.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Heterogeneous Surface Redox",
                    subtitle = "Zn(s) → Zn²⁺ + 2e⁻",
                    explanation = "Zinc atoms on the metallic lattice surrender valence electrons to hydronium ions in solution.",
                    thermodynamicDetail = "Standard cell potential E°cell = +0.76 V (spontaneous).",
                    safetyNote = "Hydrogen gas is flammable; ventilate properly."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_CUSO4_NAOH",
            title = "Cupric Hydroxide Precipitation",
            reactantIds = setOf("CuSO4", "NaOH"),
            balancedEquation = "CuSO₄(aq) + 2NaOH(aq) → Cu(OH)₂(s) ↓ + Na₂SO₄(aq)",
            animationType = ReactionAnimationType.PRECIPITATION,
            deltaH = "-48.1 kJ/mol (Lattice Enthalpy)",
            tempTrajectoryC = Pair(24.0, 28.5),
            phTrajectory = Pair(12.5, 9.8),
            observationNotes = "Immediate transformation into a magnificent gelatinous sky-blue precipitate of copper(II) hydroxide that slowly sediments.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Ionic Coordination Collision",
                    subtitle = "Ksp = 2.2 × 10⁻²⁰",
                    explanation = "Hydrated Cu²⁺ ions collide with nucleophilic OH⁻ ions. The ionic product exceeds the microscopic solubility product Ksp.",
                    thermodynamicDetail = "Lattice energy of solid Cu(OH)2 overcomes solvation energy.",
                    safetyNote = "Dispose of heavy metal copper waste in designated chemical containment."
                )
            )
        ),
        ChemicalReaction(
            id = "RXN_CACO3_HCL",
            title = "Carbonate Acid Fizzing",
            reactantIds = setOf("CaCO3", "HCl"),
            balancedEquation = "CaCO₃(s) + 2HCl(aq) → CaCl₂(aq) + H₂O(l) + CO₂(g) ↑",
            animationType = ReactionAnimationType.EFFERVESCENCE,
            deltaH = "-16.1 kJ/mol",
            tempTrajectoryC = Pair(24.0, 31.0),
            phTrajectory = Pair(1.2, 5.2),
            observationNotes = "Intense frothing and hiss as carbon dioxide gas escapes. Chalk / marble granules slowly dissolve.",
            steps = listOf(
                ReactionStep(
                    stepNumber = 1,
                    title = "Carbonic Acid Unstable Intermediate",
                    subtitle = "CaCO3 + 2H⁺ → Ca²⁺ + H2CO3 → H2O + CO2",
                    explanation = "Acid protonates carbonate into carbonic acid, which instantly decomposes into water and carbon dioxide gas.",
                    thermodynamicDetail = "Le Chatelier's shift driven by gaseous CO2 departure.",
                    safetyNote = "Causes pressure buildup in sealed vessels."
                )
            )
        )
    )

    val mathTopics = listOf(
        MathTopic(
            id = "algebraic_identity",
            title = "Algebraic Identity: (a + b)²",
            category = MathCategory.ALGEBRA,
            formula = "(a + b)² = a² + 2ab + b²",
            summary = "Geometric visual decomposition of the square of a binomial sum into 4 sub-sections.",
            detailedProof = "Consider a large square whose side length is partitioned into two segments of lengths 'a' and 'b'. The total side length is (a + b), so the total area of this geometric square is (a + b)². By extending dividing lines across the square, it splits into 4 distinct quadrants: one square of area a², one square of area b², and two symmetric rectangles each of area a × b. Summing these areas yields a² + ab + ab + b² = a² + 2ab + b²."
        ),
        MathTopic(
            id = "pythagorean_theorem",
            title = "Pythagorean Theorem",
            category = MathCategory.GEOMETRY,
            formula = "a² + b² = c²",
            summary = "Fundamental relation in Euclidean geometry among the three sides of a right triangle.",
            detailedProof = "In any right-angled triangle with leg lengths 'a' and 'b' and hypotenuse 'c', the area of the square constructed on the hypotenuse is equal to the sum of the areas of the squares on the other two sides. Proven geometrically by Garfield, Euclid, and dynamic shearing."
        ),
        MathTopic(
            id = "quadratic_equation",
            title = "Quadratic Function & Parabola",
            category = MathCategory.ALGEBRA,
            formula = "y = ax² + bx + c  |  x = (-b ± √(b² - 4ac)) / (2a)",
            summary = "Parabolic symmetry, vertex trajectory, roots, and discriminant analysis.",
            detailedProof = "Completing the square on ax² + bx + c = 0 leads directly to the quadratic formula. The discriminant Δ = b² - 4ac dictates whether the curve crosses the x-axis twice (Δ > 0), touches at one vertex (Δ = 0), or floats without real roots (Δ < 0)."
        ),
        MathTopic(
            id = "unit_circle_trig",
            title = "Unit Circle Trigonometry",
            category = MathCategory.TRIGONOMETRY,
            formula = "cos²(θ) + sin²(θ) = 1  |  tan(θ) = sin(θ) / cos(θ)",
            summary = "Trigonometric functions parameterized on a circle of radius 1 centered at the origin.",
            detailedProof = "Any point on the unit circle has Cartesian coordinates (x, y) = (cos θ, sin θ). By the Pythagorean theorem on the right triangle formed with the origin, x² + y² = 1, leading to the fundamental trigonometric identity."
        ),
        MathTopic(
            id = "calculus_derivative",
            title = "Calculus: Derivative Slope",
            category = MathCategory.CALCULUS,
            formula = "f'(x) = lim[Δx → 0] (f(x + Δx) - f(x)) / Δx",
            summary = "Instantaneous rate of change visualized as the limiting secant line turning into the tangent line.",
            detailedProof = "As the secant interval Δx shrinks toward 0, the chord connecting (x, f(x)) to (x + Δx, f(x + Δx)) converges to the exact tangent line slope at point x."
        )
    )

    fun findReaction(reagentIds: Set<String>): ChemicalReaction? {
        return reactions.firstOrNull { rxn ->
            rxn.reactantIds.all { it in reagentIds }
        }
    }
}
