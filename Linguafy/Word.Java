@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private String translation;
    private String pronunciation;
    private String audioUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}