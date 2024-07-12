--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2024-07-11 23:15:18 -05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3629 (class 1262 OID 17868)
-- Name: battleship; Type: DATABASE; Schema: -; Owner: asve
--

CREATE DATABASE battleship WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = icu LOCALE = 'en_US.UTF-8' ICU_LOCALE = 'en-US';


ALTER DATABASE battleship OWNER TO asve;

\connect battleship

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 17877)
-- Name: opciones; Type: TABLE; Schema: public; Owner: asve
--

CREATE TABLE public.opciones (
    opcion_id integer NOT NULL,
    pregunta_id integer,
    texto character varying(500) NOT NULL,
    es_correcta boolean NOT NULL
);


ALTER TABLE public.opciones OWNER TO asve;

--
-- TOC entry 217 (class 1259 OID 17876)
-- Name: opciones_opcion_id_seq; Type: SEQUENCE; Schema: public; Owner: asve
--

CREATE SEQUENCE public.opciones_opcion_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.opciones_opcion_id_seq OWNER TO asve;

--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 217
-- Name: opciones_opcion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: asve
--

ALTER SEQUENCE public.opciones_opcion_id_seq OWNED BY public.opciones.opcion_id;


--
-- TOC entry 216 (class 1259 OID 17870)
-- Name: preguntas; Type: TABLE; Schema: public; Owner: asve
--

CREATE TABLE public.preguntas (
    pregunta_id integer NOT NULL,
    texto character varying(500) NOT NULL
);


ALTER TABLE public.preguntas OWNER TO asve;

--
-- TOC entry 215 (class 1259 OID 17869)
-- Name: preguntas_pregunta_id_seq; Type: SEQUENCE; Schema: public; Owner: asve
--

CREATE SEQUENCE public.preguntas_pregunta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.preguntas_pregunta_id_seq OWNER TO asve;

--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 215
-- Name: preguntas_pregunta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: asve
--

ALTER SEQUENCE public.preguntas_pregunta_id_seq OWNED BY public.preguntas.pregunta_id;


--
-- TOC entry 3471 (class 2604 OID 17880)
-- Name: opciones opcion_id; Type: DEFAULT; Schema: public; Owner: asve
--

ALTER TABLE ONLY public.opciones ALTER COLUMN opcion_id SET DEFAULT nextval('public.opciones_opcion_id_seq'::regclass);


--
-- TOC entry 3470 (class 2604 OID 17873)
-- Name: preguntas pregunta_id; Type: DEFAULT; Schema: public; Owner: asve
--

ALTER TABLE ONLY public.preguntas ALTER COLUMN pregunta_id SET DEFAULT nextval('public.preguntas_pregunta_id_seq'::regclass);


--
-- TOC entry 3623 (class 0 OID 17877)
-- Dependencies: 218
-- Data for Name: opciones; Type: TABLE DATA; Schema: public; Owner: asve
--

INSERT INTO public.opciones VALUES (1, 1, 'París', true);
INSERT INTO public.opciones VALUES (2, 1, 'Londres', false);
INSERT INTO public.opciones VALUES (3, 1, 'Berlín', false);
INSERT INTO public.opciones VALUES (4, 1, 'Madrid', false);
INSERT INTO public.opciones VALUES (5, 2, 'Mercurio', true);
INSERT INTO public.opciones VALUES (6, 2, 'Venus', false);
INSERT INTO public.opciones VALUES (7, 2, 'Tierra', false);
INSERT INTO public.opciones VALUES (8, 2, 'Marte', false);
INSERT INTO public.opciones VALUES (9, 3, 'Thomas Edison', true);
INSERT INTO public.opciones VALUES (10, 3, 'Nikola Tesla', false);
INSERT INTO public.opciones VALUES (11, 3, 'Alexander Graham Bell', false);
INSERT INTO public.opciones VALUES (12, 4, 'Vaticano', true);
INSERT INTO public.opciones VALUES (13, 4, 'Mónaco', false);
INSERT INTO public.opciones VALUES (14, 4, 'San Marino', false);
INSERT INTO public.opciones VALUES (15, 4, 'Liechtenstein', false);
INSERT INTO public.opciones VALUES (16, 5, 'Mamífero', true);
INSERT INTO public.opciones VALUES (17, 5, 'Pez', false);
INSERT INTO public.opciones VALUES (18, 5, 'Anfibio', false);
INSERT INTO public.opciones VALUES (19, 5, 'Reptil', false);
INSERT INTO public.opciones VALUES (20, 6, 'Dólar', true);
INSERT INTO public.opciones VALUES (21, 6, 'Euro', false);
INSERT INTO public.opciones VALUES (22, 6, 'Libra', false);
INSERT INTO public.opciones VALUES (23, 6, 'Yen', false);
INSERT INTO public.opciones VALUES (24, 7, 'Neil Armstrong', true);
INSERT INTO public.opciones VALUES (25, 7, 'Buzz Aldrin', false);
INSERT INTO public.opciones VALUES (26, 7, 'Yuri Gagarin', false);
INSERT INTO public.opciones VALUES (27, 7, 'Alan Shepard', false);
INSERT INTO public.opciones VALUES (28, 8, 'Oxígeno', true);
INSERT INTO public.opciones VALUES (29, 8, 'Dióxido de carbono', false);
INSERT INTO public.opciones VALUES (30, 8, 'Nitrógeno', false);
INSERT INTO public.opciones VALUES (31, 8, 'Helio', false);
INSERT INTO public.opciones VALUES (32, 9, 'Corazón', true);
INSERT INTO public.opciones VALUES (33, 9, 'Pulmón', false);
INSERT INTO public.opciones VALUES (34, 9, 'Hígado', false);
INSERT INTO public.opciones VALUES (35, 9, 'Riñón', false);
INSERT INTO public.opciones VALUES (36, 10, 'Tres', true);
INSERT INTO public.opciones VALUES (37, 10, 'Cuatro', false);
INSERT INTO public.opciones VALUES (38, 10, 'Cinco', false);
INSERT INTO public.opciones VALUES (39, 10, 'Seis', false);
INSERT INTO public.opciones VALUES (40, 11, 'Fémur', true);
INSERT INTO public.opciones VALUES (41, 11, 'Tibia', false);
INSERT INTO public.opciones VALUES (42, 11, 'Húmero', false);
INSERT INTO public.opciones VALUES (43, 11, 'Radio', false);
INSERT INTO public.opciones VALUES (44, 12, 'Siete', true);
INSERT INTO public.opciones VALUES (45, 12, 'Seis', false);
INSERT INTO public.opciones VALUES (46, 12, 'Cinco', false);
INSERT INTO public.opciones VALUES (47, 12, 'Ocho', false);
INSERT INTO public.opciones VALUES (48, 13, 'Amazonas', true);
INSERT INTO public.opciones VALUES (49, 13, 'Orinoco', false);
INSERT INTO public.opciones VALUES (50, 13, 'Paraná', false);
INSERT INTO public.opciones VALUES (51, 13, 'Magdalena', false);
INSERT INTO public.opciones VALUES (52, 14, 'Asia', true);
INSERT INTO public.opciones VALUES (53, 14, 'África', false);
INSERT INTO public.opciones VALUES (54, 14, 'Europa', false);
INSERT INTO public.opciones VALUES (55, 14, 'América', false);
INSERT INTO public.opciones VALUES (56, 15, 'Camaleón', true);
INSERT INTO public.opciones VALUES (57, 15, 'Pulpo', false);
INSERT INTO public.opciones VALUES (58, 15, 'Cangrejo ermitaño', false);
INSERT INTO public.opciones VALUES (59, 15, 'Cocodrilo', false);
INSERT INTO public.opciones VALUES (60, 16, 'Tornado', true);
INSERT INTO public.opciones VALUES (61, 16, 'Huracán', false);
INSERT INTO public.opciones VALUES (62, 16, 'Terremoto', false);
INSERT INTO public.opciones VALUES (63, 16, 'Tsunami', false);
INSERT INTO public.opciones VALUES (64, 17, '366', true);
INSERT INTO public.opciones VALUES (65, 17, '365', false);
INSERT INTO public.opciones VALUES (66, 17, '360', false);
INSERT INTO public.opciones VALUES (67, 17, '367', false);
INSERT INTO public.opciones VALUES (68, 18, 'Paloma', true);
INSERT INTO public.opciones VALUES (69, 18, 'Delfín', false);
INSERT INTO public.opciones VALUES (70, 18, 'Elefante', false);
INSERT INTO public.opciones VALUES (71, 18, 'León', false);
INSERT INTO public.opciones VALUES (72, 19, 'Hidrógeno', true);
INSERT INTO public.opciones VALUES (73, 19, 'Helio', false);
INSERT INTO public.opciones VALUES (74, 19, 'Hierro', false);
INSERT INTO public.opciones VALUES (75, 19, 'Oxígeno', false);
INSERT INTO public.opciones VALUES (76, 20, 'Siete', true);
INSERT INTO public.opciones VALUES (77, 20, 'Seis', false);
INSERT INTO public.opciones VALUES (78, 20, 'Ocho', false);
INSERT INTO public.opciones VALUES (79, 20, 'Cinco', false);
INSERT INTO public.opciones VALUES (80, 21, 'Leonardo da Vinci', true);
INSERT INTO public.opciones VALUES (81, 21, 'Pablo Picasso', false);
INSERT INTO public.opciones VALUES (82, 21, 'Vincent van Gogh', false);
INSERT INTO public.opciones VALUES (83, 21, 'Claude Monet', false);
INSERT INTO public.opciones VALUES (84, 22, 'Aproximadamente 97%', true);
INSERT INTO public.opciones VALUES (85, 22, 'Aproximadamente 75%', false);
INSERT INTO public.opciones VALUES (86, 22, 'Aproximadamente 50%', false);
INSERT INTO public.opciones VALUES (87, 22, 'Aproximadamente 25%', false);
INSERT INTO public.opciones VALUES (88, 23, '1939', true);
INSERT INTO public.opciones VALUES (89, 23, '1941', false);
INSERT INTO public.opciones VALUES (90, 23, '1945', false);
INSERT INTO public.opciones VALUES (91, 23, '1929', false);
INSERT INTO public.opciones VALUES (92, 24, 'George Washington', true);
INSERT INTO public.opciones VALUES (93, 24, 'Abraham Lincoln', false);
INSERT INTO public.opciones VALUES (94, 24, 'Thomas Jefferson', false);
INSERT INTO public.opciones VALUES (95, 24, 'John Adams', false);
INSERT INTO public.opciones VALUES (96, 25, 'Conocimiento', true);
INSERT INTO public.opciones VALUES (97, 25, 'Habilidad', false);
INSERT INTO public.opciones VALUES (98, 25, 'Tiempo', false);
INSERT INTO public.opciones VALUES (99, 25, 'Pregunta', false);
INSERT INTO public.opciones VALUES (100, 26, '7', true);
INSERT INTO public.opciones VALUES (101, 26, '10', false);
INSERT INTO public.opciones VALUES (102, 26, '15', false);
INSERT INTO public.opciones VALUES (103, 26, '20', false);
INSERT INTO public.opciones VALUES (104, 27, 'Personificación', true);
INSERT INTO public.opciones VALUES (105, 27, 'Metáfora', false);
INSERT INTO public.opciones VALUES (106, 27, 'Símil', false);
INSERT INTO public.opciones VALUES (107, 27, 'Hipérbole', false);
INSERT INTO public.opciones VALUES (108, 28, 'Rima', true);
INSERT INTO public.opciones VALUES (109, 28, 'Estrofa', false);
INSERT INTO public.opciones VALUES (110, 28, 'Verso', false);
INSERT INTO public.opciones VALUES (111, 28, 'Léxico', false);
INSERT INTO public.opciones VALUES (112, 29, '28', true);
INSERT INTO public.opciones VALUES (113, 29, '30', false);
INSERT INTO public.opciones VALUES (114, 29, '25', false);
INSERT INTO public.opciones VALUES (115, 29, '35', false);
INSERT INTO public.opciones VALUES (116, 30, '60', true);
INSERT INTO public.opciones VALUES (117, 30, '30', false);
INSERT INTO public.opciones VALUES (118, 30, '45', false);
INSERT INTO public.opciones VALUES (119, 30, '90', false);
INSERT INTO public.opciones VALUES (120, 31, '25', true);
INSERT INTO public.opciones VALUES (121, 31, '20', false);
INSERT INTO public.opciones VALUES (122, 31, '30', false);
INSERT INTO public.opciones VALUES (123, 31, '10', false);
INSERT INTO public.opciones VALUES (124, 32, 'Corteza', true);
INSERT INTO public.opciones VALUES (125, 32, 'Manto', false);
INSERT INTO public.opciones VALUES (126, 32, 'Núcleo externo', false);
INSERT INTO public.opciones VALUES (127, 32, 'Núcleo interno', false);
INSERT INTO public.opciones VALUES (128, 33, 'Fotosíntesis', true);
INSERT INTO public.opciones VALUES (129, 33, 'Respiración', false);
INSERT INTO public.opciones VALUES (130, 33, 'Germinación', false);
INSERT INTO public.opciones VALUES (131, 33, 'Transpiración', false);
INSERT INTO public.opciones VALUES (132, 34, 'Energía solar', true);
INSERT INTO public.opciones VALUES (133, 34, 'Energía eólica', false);
INSERT INTO public.opciones VALUES (134, 34, 'Energía nuclear', false);
INSERT INTO public.opciones VALUES (135, 34, 'Energía hidráulica', false);
INSERT INTO public.opciones VALUES (136, 35, 'Célula', true);
INSERT INTO public.opciones VALUES (137, 35, 'Átomo', false);
INSERT INTO public.opciones VALUES (138, 35, 'Molécula', false);
INSERT INTO public.opciones VALUES (139, 35, 'Tejido', false);
INSERT INTO public.opciones VALUES (140, 36, 'Evaporación', true);
INSERT INTO public.opciones VALUES (141, 36, 'Condensación', false);
INSERT INTO public.opciones VALUES (142, 36, 'Sublimación', false);
INSERT INTO public.opciones VALUES (143, 36, 'Fusión', false);
INSERT INTO public.opciones VALUES (144, 37, 'Absorción', true);
INSERT INTO public.opciones VALUES (145, 37, 'Transpiración', false);
INSERT INTO public.opciones VALUES (146, 37, 'Fotosíntesis', false);
INSERT INTO public.opciones VALUES (147, 37, 'Germinación', false);
INSERT INTO public.opciones VALUES (148, 38, 'Segunda Guerra Mundial', true);
INSERT INTO public.opciones VALUES (149, 38, 'Primera Guerra Mundial', false);
INSERT INTO public.opciones VALUES (150, 38, 'Guerra de Vietnam', false);
INSERT INTO public.opciones VALUES (151, 38, 'Guerra Fría', false);
INSERT INTO public.opciones VALUES (152, 39, 'Termómetro', true);
INSERT INTO public.opciones VALUES (153, 39, 'Barómetro', false);
INSERT INTO public.opciones VALUES (154, 39, 'Higrómetro', false);
INSERT INTO public.opciones VALUES (155, 39, 'Cronómetro', false);
INSERT INTO public.opciones VALUES (156, 40, 'Rusia', true);
INSERT INTO public.opciones VALUES (157, 40, 'Canadá', false);
INSERT INTO public.opciones VALUES (158, 40, 'China', false);
INSERT INTO public.opciones VALUES (159, 40, 'Estados Unidos', false);
INSERT INTO public.opciones VALUES (160, 41, 'Marte', true);
INSERT INTO public.opciones VALUES (161, 41, 'Venus', false);
INSERT INTO public.opciones VALUES (162, 41, 'Júpiter', false);
INSERT INTO public.opciones VALUES (163, 41, 'Mercurio', false);
INSERT INTO public.opciones VALUES (164, 42, 'Adjetivo', true);
INSERT INTO public.opciones VALUES (165, 42, 'Verbo', false);
INSERT INTO public.opciones VALUES (166, 43, 'Verbo', true);
INSERT INTO public.opciones VALUES (167, 43, 'Adjetivo', false);
INSERT INTO public.opciones VALUES (168, 44, 'Went', true);
INSERT INTO public.opciones VALUES (169, 44, 'Gone', false);
INSERT INTO public.opciones VALUES (170, 44, 'Goed', false);
INSERT INTO public.opciones VALUES (171, 44, 'Going', false);
INSERT INTO public.opciones VALUES (172, 45, 'Ecuador', true);
INSERT INTO public.opciones VALUES (173, 45, 'Perú', false);
INSERT INTO public.opciones VALUES (174, 45, 'Colombia', false);
INSERT INTO public.opciones VALUES (175, 45, 'Brasil', false);
INSERT INTO public.opciones VALUES (176, 46, 'William Shakespeare', true);
INSERT INTO public.opciones VALUES (177, 46, 'Charles Dickens', false);
INSERT INTO public.opciones VALUES (178, 46, 'Jane Austen', false);
INSERT INTO public.opciones VALUES (179, 46, 'Mark Twain', false);
INSERT INTO public.opciones VALUES (180, 47, 'Estribo', true);
INSERT INTO public.opciones VALUES (181, 47, 'Fémur', false);
INSERT INTO public.opciones VALUES (182, 47, 'Cráneo', false);
INSERT INTO public.opciones VALUES (183, 47, 'Tibia', false);
INSERT INTO public.opciones VALUES (184, 48, '206', true);
INSERT INTO public.opciones VALUES (185, 48, '180', false);
INSERT INTO public.opciones VALUES (186, 48, '300', false);
INSERT INTO public.opciones VALUES (187, 48, '120', false);
INSERT INTO public.opciones VALUES (188, 49, 'Italia', true);
INSERT INTO public.opciones VALUES (189, 49, 'España', false);
INSERT INTO public.opciones VALUES (190, 49, 'Francia', false);
INSERT INTO public.opciones VALUES (191, 49, 'Alemania', false);
INSERT INTO public.opciones VALUES (192, 50, 'Ocho', true);
INSERT INTO public.opciones VALUES (193, 50, 'Seis', false);
INSERT INTO public.opciones VALUES (194, 50, 'Diez', false);
INSERT INTO public.opciones VALUES (195, 50, 'Doce', false);


--
-- TOC entry 3621 (class 0 OID 17870)
-- Dependencies: 216
-- Data for Name: preguntas; Type: TABLE DATA; Schema: public; Owner: asve
--

INSERT INTO public.preguntas VALUES (1, '¿Cuál es la capital de Francia?');
INSERT INTO public.preguntas VALUES (2, '¿Qué planeta es el más cercano al sol?');
INSERT INTO public.preguntas VALUES (3, '¿Quién inventó la bombilla eléctrica?');
INSERT INTO public.preguntas VALUES (4, '¿Cuál es el país más pequeño del mundo?');
INSERT INTO public.preguntas VALUES (5, '¿Qué tipo de animal es la ballena?');
INSERT INTO public.preguntas VALUES (6, '¿Cuál es la moneda de Estados Unidos?');
INSERT INTO public.preguntas VALUES (7, '¿Quién fue el primer hombre en pisar la luna?');
INSERT INTO public.preguntas VALUES (8, '¿Qué gas es necesario para respirar?');
INSERT INTO public.preguntas VALUES (9, '¿Qué órgano del cuerpo humano bombea sangre?');
INSERT INTO public.preguntas VALUES (10, '¿Cuántos lados tiene un triángulo?');
INSERT INTO public.preguntas VALUES (11, '¿Cuál es el hueso más largo del cuerpo humano?');
INSERT INTO public.preguntas VALUES (12, '¿Cuántos continentes hay en la Tierra?');
INSERT INTO public.preguntas VALUES (13, '¿Cuál es el río más largo de América del Sur?');
INSERT INTO public.preguntas VALUES (14, '¿Cuál es el continente más grande?');
INSERT INTO public.preguntas VALUES (15, '¿Qué animal es conocido por cambiar de color para camuflarse?');
INSERT INTO public.preguntas VALUES (16, '¿Qué fenómeno natural es conocido por su forma de embudo y vientos muy fuertes?');
INSERT INTO public.preguntas VALUES (17, '¿Cuántos días tiene un año bisiesto?');
INSERT INTO public.preguntas VALUES (18, '¿Qué animal es el símbolo de la paz?');
INSERT INTO public.preguntas VALUES (19, '¿Qué elemento químico tiene el símbolo "H"?');
INSERT INTO public.preguntas VALUES (20, '¿Cuántos colores tiene el arcoíris?');
INSERT INTO public.preguntas VALUES (21, '¿Quién pintó la Mona Lisa?');
INSERT INTO public.preguntas VALUES (22, '¿Qué porcentaje del agua en la Tierra es agua salada?');
INSERT INTO public.preguntas VALUES (23, '¿En qué año comenzó la Segunda Guerra Mundial?');
INSERT INTO public.preguntas VALUES (24, '¿Quién fue el primer presidente de Estados Unidos?');
INSERT INTO public.preguntas VALUES (25, '¿Cuál es el significado de la palabra "knowledge" en inglés?');
INSERT INTO public.preguntas VALUES (26, '¿Cuál de los siguientes números es primo?');
INSERT INTO public.preguntas VALUES (27, '¿Qué figura literaria consiste en dar características humanas a objetos o animales?');
INSERT INTO public.preguntas VALUES (28, '¿Cuál es el nombre del conjunto de palabras que rima al final de los versos de un poema?');
INSERT INTO public.preguntas VALUES (29, '¿Cuál es el resultado de multiplicar 7 por 4?');
INSERT INTO public.preguntas VALUES (30, '¿Cuántos minutos hay en una hora?');
INSERT INTO public.preguntas VALUES (31, '¿Cuál es el área de un cuadrado con lado de longitud 5 unidades?');
INSERT INTO public.preguntas VALUES (32, '¿Cuál es la capa más externa de la Tierra?');
INSERT INTO public.preguntas VALUES (33, '¿Cuál es el proceso mediante el cual las plantas producen su propio alimento?');
INSERT INTO public.preguntas VALUES (34, '¿Qué tipo de energía proviene del sol y es fundamental para la vida en la Tierra?');
INSERT INTO public.preguntas VALUES (35, '¿Cuál es la unidad básica de la estructura de los seres vivos?');
INSERT INTO public.preguntas VALUES (36, '¿Cuál es el nombre del proceso por el cual el agua en forma líquida se convierte en vapor?');
INSERT INTO public.preguntas VALUES (37, '¿Cuál es el nombre del proceso por el cual las plantas toman agua y nutrientes a través de sus raíces?');
INSERT INTO public.preguntas VALUES (38, '¿Qué evento histórico terminó en 1945?');
INSERT INTO public.preguntas VALUES (39, '¿Qué instrumento se utiliza para medir la temperatura del cuerpo humano?');
INSERT INTO public.preguntas VALUES (40, '¿Cuál es el país más grande del mundo por área terrestre?');
INSERT INTO public.preguntas VALUES (41, '¿Cuál es el planeta conocido como "el planeta rojo"?');
INSERT INTO public.preguntas VALUES (42, 'Identifica la palabra "happy".');
INSERT INTO public.preguntas VALUES (43, 'Identifica la palabra "sing".');
INSERT INTO public.preguntas VALUES (44, '¿Cuál es el tiempo pasado de la palabra "go"?');
INSERT INTO public.preguntas VALUES (45, '¿Dónde está ubicado el centro del mundo?');
INSERT INTO public.preguntas VALUES (46, '¿Quién escribió "Romeo y Julieta"?');
INSERT INTO public.preguntas VALUES (47, '¿Cuál es el hueso más pequeño del cuerpo humano?');
INSERT INTO public.preguntas VALUES (48, '¿Cuántos huesos tiene un ser humano adulto aproximadamente?');
INSERT INTO public.preguntas VALUES (49, '¿Qué país tiene forma de bota en el mapa?');
INSERT INTO public.preguntas VALUES (50, '¿Cuántas patas tiene una araña?');


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 217
-- Name: opciones_opcion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: asve
--

SELECT pg_catalog.setval('public.opciones_opcion_id_seq', 195, true);


--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 215
-- Name: preguntas_pregunta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: asve
--

SELECT pg_catalog.setval('public.preguntas_pregunta_id_seq', 50, true);


--
-- TOC entry 3475 (class 2606 OID 17884)
-- Name: opciones opciones_pkey; Type: CONSTRAINT; Schema: public; Owner: asve
--

ALTER TABLE ONLY public.opciones
    ADD CONSTRAINT opciones_pkey PRIMARY KEY (opcion_id);


--
-- TOC entry 3473 (class 2606 OID 17875)
-- Name: preguntas preguntas_pkey; Type: CONSTRAINT; Schema: public; Owner: asve
--

ALTER TABLE ONLY public.preguntas
    ADD CONSTRAINT preguntas_pkey PRIMARY KEY (pregunta_id);


--
-- TOC entry 3476 (class 2606 OID 17885)
-- Name: opciones opciones_pregunta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: asve
--

ALTER TABLE ONLY public.opciones
    ADD CONSTRAINT opciones_pregunta_id_fkey FOREIGN KEY (pregunta_id) REFERENCES public.preguntas(pregunta_id) ON DELETE CASCADE;


-- Completed on 2024-07-11 23:15:19 -05

--
-- PostgreSQL database dump complete
--

